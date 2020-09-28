package com.gapache.blog.server.api.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.blog.common.model.dto.*;
import com.gapache.blog.common.util.ParseMarkdownUtils;
import com.gapache.blog.server.api.BlogApiService;
import com.gapache.blog.server.dao.data.BlogData;
import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogEsRepository;
import com.gapache.blog.server.lua.BlogLuaScript;
import com.gapache.blog.server.service.BlogService;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import com.gapache.redis.RedisLuaExecutor;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

import static com.gapache.blog.server.dao.data.Structures.*;

/**
 * @author HuSen
 * create on 2020/4/5 03:33
 */
@Slf4j
@Service
public class BlogApiServiceImpl implements BlogApiService {

    private final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    @PostConstruct
    public void init() {
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadFactory(r -> new Thread(r, "sync thread"));
        taskExecutor.initialize();
    }

    private final StringRedisTemplate stringRedisTemplate;
    private final BlogEsRepository blogEsRepository;
    private final BlogService blogService;
    private final RedisLuaExecutor luaExecutor;

    public BlogApiServiceImpl(StringRedisTemplate stringRedisTemplate, BlogEsRepository blogEsRepository, BlogService blogService, RedisLuaExecutor luaExecutor) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.blogEsRepository = blogEsRepository;
        this.blogService = blogService;
        this.luaExecutor = luaExecutor;
    }

    @Override
    public Boolean create(BlogDTO blog) {
        log.info("Create Blog:[id:{}, title:{}, introduction:{}, publishTime:{}, category:{}, tags:{}]",
                blog.getId(), blog.getTitle(), blog.getIntroduction(), blog.getPublishTime(), blog.getCategory(), Arrays.toString(blog.getTags()));

        return saveOrUpdate(blog, false);
    }

    @Override
    public SimpleBlogDTO get(String id) {
        String json = stringRedisTemplate.opsForValue().get(BLOG.key(id));
        if (StringUtils.isNotBlank(json)) {
            return JSON.parseObject(json, SimpleBlogDTO.class);
        }
        return null;
    }

    @Override
    public boolean delete(String id) {
        log.info("Delete Blog:{}", id);

        String result = luaExecutor.execute(BlogLuaScript.DELETE, Lists.newArrayList(BLOG.key(id), CONTENT.key(id), IDS.key(), CATEGORIES.key(), TAGS.key(), VIEWS.key()), id);
        if (!BlogLuaScript.OK.equals(result)) {
            return false;
        }
        blogEsRepository.delete(id);
        return true;
    }

    @Override
    public Boolean update(BlogDTO blog) {
        log.info("Update Blog:[id:{}, title:{}, introduction:{}, publishTime:{}, category:{}, tags:{}]",
                blog.getId(), blog.getTitle(), blog.getIntroduction(), blog.getPublishTime(), blog.getCategory(), Arrays.toString(blog.getTags()));
        return saveOrUpdate(blog, true);
    }

    private boolean saveOrUpdate(BlogDTO blog, boolean update) {
        String md = IStringUtils.newString(blog.getContent());
        log.info("要解析的md为:{}", md);
        List<MarkdownItemDTO> items = ParseMarkdownUtils.parse(md);
        log.info("md解析结果:{}", JSON.toJSONString(items));
        MarkdownDTO markdownDTO = new MarkdownDTO();
        markdownDTO.setItems(items);
        byte[] bytes = ProtocstuffUtils.bean2Byte(markdownDTO, MarkdownDTO.class);

        BlogData data = new BlogData();
        BeanUtils.copyProperties(blog, data, "content");
        String jsonString = JSON.toJSONString(data);
        String result;
        if (update) {
            result = luaExecutor.execute(BlogLuaScript.UPDATE, RedisSerializer.byteArray(), Lists.newArrayList(BLOG.key(blog.getId()), CONTENT.key(blog.getId()), IDS.key(), CATEGORIES.key(), TAGS.key()),
                    IStringUtils.getBytes(jsonString), bytes, IStringUtils.getBytes(blog.getId()));
        } else {
            result = luaExecutor.execute(BlogLuaScript.CREATE, RedisSerializer.byteArray(), Lists.newArrayList(BLOG.key(blog.getId()), CONTENT.key(blog.getId()), IDS.key(), CATEGORIES.key(), TAGS.key(), VIEWS.key()),
                    IStringUtils.getBytes(jsonString), bytes, IStringUtils.getBytes(blog.getId()));

        }
        log.info("是否更新{}博客到Redis结果:{}", update, result);
        if (!BlogLuaScript.OK.equals(result)) {
            return false;
        }

        Blog document = new Blog();
        BeanUtils.copyProperties(blog, document, "content");
        StringBuilder contentBuilder = new StringBuilder();
        for (MarkdownItemDTO item : items) {
            Object content = item.getContent();
            if (content != null) {
                contentBuilder.append(content.toString());
            }
        }
        document.setContent(contentBuilder.toString());
        if (update) {
            blogEsRepository.update(document);
        } else {
            blogEsRepository.index(document);
        }
        return true;
    }

    @Override
    public PageResult<SimpleBlogDTO> findAll(IPageRequest<BlogQueryDTO> iPageRequest) {
        PageResult<SimpleBlogDTO> result = PageResult.empty();

        stringRedisTemplate.execute((RedisCallback<Object>) connection ->
        {
            byte[] keyBytes = IDS.keyBytes();
            Long count = connection.zCount(keyBytes, 0, 1);
            if (count == null) {
                return null;
            }
            result.setTotal(count);

            Integer page = iPageRequest.getPage();
            Integer number = iPageRequest.getNumber();
            long start = (page - 1) * number;
            long end = Math.min((start + number - 1), count - 1);
            Set<byte[]> bytes = connection.zRange(keyBytes, start, end);
            if (bytes == null) {
                return null;
            }
            Map<String, Integer> scoreMap = new HashMap<>(bytes.size());
            // TODO 优化
            for (byte[] aByte : bytes) {
                Double score = connection.zScore(VIEWS.keyBytes(), aByte);
                scoreMap.put(IStringUtils.newString(aByte), score == null ? 0 : score.intValue());
            }
            List<SimpleBlogDTO> items = blogService.findAllByIds(scoreMap.keySet(), scoreMap);
            result.setItems(items);

            return null;
        });
        return result;
    }

    @Override
    public void sync(String id) {
        Set<String> blogIds;
        if (StringUtils.isNotBlank(id)) {
            blogIds = Sets.newHashSet(id);
        } else {
            blogIds = stringRedisTemplate.opsForZSet().range(IDS.key(), 0, -1);
        }
        taskExecutor.execute(() ->
        {
            if (StringUtils.isNotBlank(id)) {
                blogEsRepository.delete(id);
            } else {
                if (!blogEsRepository.deleteAll()) {
                    return;
                }
            }
            if (CollectionUtils.isEmpty(blogIds)) {
                return;
            }
            stringRedisTemplate.execute((RedisCallback<Object>) connection -> {
                for (String blogId : blogIds) {
                    byte[] blogData = connection.get(BLOG.keyBytes(blogId));
                    if (blogData == null) {
                        continue;
                    }
                    byte[] contentData = connection.get(CONTENT.keyBytes(blogId));
                    if (contentData == null) {
                        continue;
                    }
                    MarkdownDTO markdownDTO = ProtocstuffUtils.byte2Bean(contentData, MarkdownDTO.class);
                    List<MarkdownItemDTO> items = markdownDTO.getItems();
                    StringBuilder contentBuilder = new StringBuilder();
                    for (MarkdownItemDTO item : items) {
                        Object content = item.getContent();
                        if (content != null) {
                            contentBuilder.append(content.toString());
                        }
                    }
                    String json = IStringUtils.newString(blogData);
                    BlogData blog = JSON.parseObject(json, BlogData.class);
                    log.info("创建博客:{}", json);
                    Blog document = new Blog();
                    BeanUtils.copyProperties(blog, document, "content");
                    document.setContent(contentBuilder.toString());
                    blogEsRepository.index(document);
                }
                return null;
            });
        });
    }
}
