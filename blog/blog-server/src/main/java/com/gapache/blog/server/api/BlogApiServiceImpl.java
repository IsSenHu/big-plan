package com.gapache.blog.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.gapache.blog.sdk.dubbo.blog.BlogApiService;
import com.gapache.blog.sdk.dubbo.blog.BlogQueryVO;
import com.gapache.blog.sdk.dubbo.blog.BlogVO;
import com.gapache.blog.sdk.dubbo.blog.SimpleBlogVO;
import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogEsRepository;
import com.gapache.blog.server.dao.data.BlogData;
import com.gapache.blog.server.lua.BlogLuaScript;
import com.gapache.blog.server.service.BlogService;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.utils.IStringUtils;
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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

import static com.gapache.blog.server.dao.data.Structures.*;

/**
 * @author HuSen
 * create on 2020/4/5 03:33
 */
@Slf4j
@Service(version = "1.0.0")
@Component
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
    public Boolean create(BlogVO blog) {
        log.info("Create Blog:[id:{}, title:{}, introduction:{}, publishTime:{}, category:{}, tags:{}]",
                blog.getId(), blog.getTitle(), blog.getIntroduction(), blog.getPublishTime(), blog.getCategory(), Arrays.toString(blog.getTags()));

        // 只要这里成功了就行，下面的其他操作失败了都没关系，到时候是可以用这个真正的数据进行核对的（除了views字段）
        BlogData data = new BlogData();
        BeanUtils.copyProperties(blog, data, "content");
        String jsonString = JSON.toJSONString(data);
        String result = luaExecutor.execute(BlogLuaScript.CREATE, RedisSerializer.byteArray(), Lists.newArrayList(BLOG.key(blog.getId()), CONTENT.key(blog.getId()), IDS.key(), CATEGORIES.key(), TAGS.key(), VIEWS.key()),
                IStringUtils.getBytes(jsonString), blog.getContent(), IStringUtils.getBytes(blog.getId()));
        log.info("保存博客到Redis结果:{}", result);
        if (!BlogLuaScript.OK.equals(result)) {
            return false;
        }

        Blog document = new Blog();
        BeanUtils.copyProperties(blog, document, "content");
        document.setContent(new String(blog.getContent(), StandardCharsets.UTF_8));

        blogEsRepository.index(document);
        return true;
    }

    @Override
    public SimpleBlogVO get(String id) {
        String json = stringRedisTemplate.opsForValue().get(BLOG.key(id));
        if (StringUtils.isNotBlank(json)) {
            return JSON.parseObject(json, SimpleBlogVO.class);
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
    public Boolean update(BlogVO blog) {
        log.info("Update Blog:[id:{}, title:{}, introduction:{}, publishTime:{}, category:{}, tags:{}]",
                blog.getId(), blog.getTitle(), blog.getIntroduction(), blog.getPublishTime(), blog.getCategory(), Arrays.toString(blog.getTags()));

        BlogData data = new BlogData();
        BeanUtils.copyProperties(blog, data, "content");
        String jsonString = JSON.toJSONString(data);
        String result = luaExecutor.execute(BlogLuaScript.UPDATE, RedisSerializer.byteArray(), Lists.newArrayList(BLOG.key(blog.getId()), CONTENT.key(blog.getId()), IDS.key(), CATEGORIES.key(), TAGS.key()),
                IStringUtils.getBytes(jsonString), blog.getContent(), IStringUtils.getBytes(blog.getId()));
        if (!BlogLuaScript.OK.equals(result)) {
            return false;
        }
        Blog document = new Blog();
        BeanUtils.copyProperties(blog, document, "content");
        document.setContent(new String(blog.getContent(), StandardCharsets.UTF_8));
        blogEsRepository.update(document);
        return true;
    }

    @Override
    public PageResult<SimpleBlogVO> findAll(IPageRequest<BlogQueryVO> iPageRequest) {
        PageResult<SimpleBlogVO> result = PageResult.empty();

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
            List<SimpleBlogVO> items = blogService.findAllByIds(scoreMap.keySet(), scoreMap);
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
                    String json = IStringUtils.newString(blogData);
                    BlogData blog = JSON.parseObject(json, BlogData.class);
                    log.info("创建博客:{}", json);
                    String content = IStringUtils.newString(contentData);
                    Blog document = new Blog();
                    BeanUtils.copyProperties(blog, document, "content");
                    document.setContent(content);
                    blogEsRepository.index(document);
                }
                return null;
            });
        });
    }
}
