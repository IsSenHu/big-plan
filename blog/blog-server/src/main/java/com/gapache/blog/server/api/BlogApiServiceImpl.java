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
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gapache.blog.server.dao.data.Structures.*;

/**
 * @author HuSen
 * create on 2020/4/5 03:33
 */
@Slf4j
@Service
@Component
public class BlogApiServiceImpl implements BlogApiService {

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
    public void create(BlogVO blog) {
        log.info("Create Blog:[id:{}, title:{}, introduction:{}, publishTime:{}, category:{}, tags:{}]",
                blog.getId(), blog.getTitle(), blog.getIntroduction(), blog.getPublishTime(), blog.getCategory(), Arrays.toString(blog.getTags()));

        // 只要这里成功了就行，下面的其他操作失败了都没关系，到时候是可以用这个真正的数据进行核对的（除了views字段）
        BlogData data = new BlogData();
        BeanUtils.copyProperties(blog, data, "content");
        String jsonString = JSON.toJSONString(data);
        System.out.println(jsonString);
        String result = luaExecutor.execute(BlogLuaScript.CREATE, RedisSerializer.byteArray(), Lists.newArrayList(BLOG.key(blog.getId()), CONTENT.key(blog.getId()), IDS.key(), CATEGORIES.key(), TAGS.key()),
                IStringUtils.getBytes(jsonString), blog.getContent(), IStringUtils.getBytes(blog.getId()));
        log.info("保存博客到Redis结果:{}", result);
        if (!BlogLuaScript.OK.equals(result)) {
            return;
        }

        Blog document = new Blog();
        BeanUtils.copyProperties(blog, document, "content");
        document.setContent(new String(blog.getContent(), StandardCharsets.UTF_8));

        blogEsRepository.index(document);
    }

    @Override
    public boolean delete(String id) {
        log.info("Delete Blog:{}", id);

        String result = luaExecutor.execute(BlogLuaScript.DELETE, Lists.newArrayList(BLOG.key(id), CONTENT.key(id), IDS.key(), CATEGORIES.key(), TAGS.key()), id);
        if (!BlogLuaScript.OK.equals(result)) {
            return false;
        }
        blogEsRepository.delete(id);
        return true;
    }

    @Override
    public void update(BlogVO blog) {
        log.info("Update Blog:[id:{}, title:{}, introduction:{}, publishTime:{}, category:{}, tags:{}]",
                blog.getId(), blog.getTitle(), blog.getIntroduction(), blog.getPublishTime(), blog.getCategory(), Arrays.toString(blog.getTags()));

        BlogData data = new BlogData();
        BeanUtils.copyProperties(blog, data, "content");
        String jsonString = JSON.toJSONString(data);
        String result = luaExecutor.execute(BlogLuaScript.UPDATE, RedisSerializer.byteArray(), Lists.newArrayList(BLOG.key(blog.getId()), CONTENT.key(blog.getId()), IDS.key(), CATEGORIES.key(), TAGS.key()),
                IStringUtils.getBytes(jsonString), blog.getContent(), IStringUtils.getBytes(blog.getId()));
        if (!BlogLuaScript.OK.equals(result)) {
            return;
        }
        Blog document = new Blog();
        BeanUtils.copyProperties(blog, document, "content");
        document.setContent(new String(blog.getContent(), StandardCharsets.UTF_8));
        blogEsRepository.update(document);
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
            Set<String> ids = bytes.stream().map(IStringUtils::newString).collect(Collectors.toSet());
            List<SimpleBlogVO> items = blogService.findAllByIds(ids, Maps.newHashMap());
            result.setItems(items);

            return null;
        });
        return result;
    }
}
