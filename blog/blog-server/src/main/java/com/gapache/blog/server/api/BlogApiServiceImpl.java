package com.gapache.blog.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.gapache.blog.sdk.dubbo.blog.BlogApiService;
import com.gapache.blog.sdk.dubbo.blog.BlogQueryVO;
import com.gapache.blog.sdk.dubbo.blog.BlogVO;
import com.gapache.blog.sdk.dubbo.blog.SimpleBlogVO;
import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogEsRepository;
import com.gapache.blog.server.dao.repository.CategoryRedisRepository;
import com.gapache.blog.server.dao.repository.TagRedisRepository;
import com.gapache.blog.server.dao.data.BlogData;
import com.gapache.blog.server.service.BlogService;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.ITuple;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    private final StringRedisTemplate redisTemplate;
    private final BlogEsRepository blogEsRepository;
    private final TagRedisRepository tagRedisRepository;
    private final CategoryRedisRepository categoryRedisRepository;
    private final BlogService blogService;

    public BlogApiServiceImpl(StringRedisTemplate redisTemplate, BlogEsRepository blogEsRepository, TagRedisRepository tagRedisRepository, CategoryRedisRepository categoryRedisRepository, BlogService blogService) {
        this.redisTemplate = redisTemplate;
        this.blogEsRepository = blogEsRepository;
        this.tagRedisRepository = tagRedisRepository;
        this.categoryRedisRepository = categoryRedisRepository;
        this.blogService = blogService;
    }

    @Override
    public void create(BlogVO blog) {
        log.info("Create Blog:[id:{}, title:{}, introduction:{}, publishTime:{}, category:{}, tags:{}]",
                blog.getId(), blog.getTitle(), blog.getIntroduction(), blog.getPublishTime(), blog.getCategory(), Arrays.toString(blog.getTags()));

        // 只要这里成功了就行，下面的其他操作失败了都没关系，到时候是可以用这个真正的数据进行核对的（除了views字段）
        redisTemplate.execute((RedisCallback<Object>) connection ->
        {
            BlogData data = new BlogData();
            BeanUtils.copyProperties(blog, data, "content");
            byte[] bytes = ProtocstuffUtils.bean2Byte(data, BlogData.class);
            if (bytes != null) {
                connection.multi();
                connection.set(BLOG.keyBytes(blog.getId()), bytes);
                connection.set(CONTENT.keyBytes(blog.getId()), blog.getContent());
                connection.zAdd(IDS.keyBytes(), 0, IStringUtils.getBytes(blog.getId()));
                connection.exec();
            }
            return null;
        });

        Blog document = new Blog();
        BeanUtils.copyProperties(blog, document, "content");
        document.setContent(new String(blog.getContent(), StandardCharsets.UTF_8));

        blogEsRepository.index(document);
        tagRedisRepository.increment(blog.getTags());
        categoryRedisRepository.add(blog.getCategory());
    }

    @Override
    public boolean delete(String id) {
        log.info("Delete Blog:{}", id);

        ITuple<Boolean, BlogData> result = new ITuple<>();
        redisTemplate.execute((RedisCallback<Blog>) connection ->
        {
            byte[] keyBytes = BLOG.keyBytes(id);
            BlogData right = getBlog(connection, keyBytes);
            if (right == null) {
                result.setLeft(false);
                return null;
            }
            // TODO 修改将getBlog提到外面，并对以下的删除操作加上事务处理
            result.setRight(right);

            Long del = connection.del(keyBytes);
            Long delContent = connection.del(CONTENT.keyBytes(id));
            Long zRem = connection.zRem(IDS.keyBytes(), IStringUtils.getBytes(id));
            log.info("{}.{}.{}", del, delContent, zRem);

            result.setLeft(del != null && del > 0 && zRem != null && zRem > 0 && delContent !=null && delContent > 0);
            return null;
        });
        if (!result.getLeft()) {
            return false;
        }
        BlogData data = result.getRight();
        boolean delete = blogEsRepository.delete(id);
        if (delete) {
            tagRedisRepository.decrement(data.getTags());
            categoryRedisRepository.delete(data.getCategory());
        }
        return delete;
    }

    @Override
    public void update(BlogVO blog) {
        log.info("Update Blog:[id:{}, title:{}, introduction:{}, publishTime:{}, category:{}, tags:{}]",
                blog.getId(), blog.getTitle(), blog.getIntroduction(), blog.getPublishTime(), blog.getCategory(), Arrays.toString(blog.getTags()));

        ITuple<Boolean, BlogData> result = new ITuple<>();
        redisTemplate.execute((RedisCallback<Boolean>) connection ->
        {
            byte[] keyBytes = BLOG.keyBytes(blog.getId());
            Boolean exists = connection.exists(keyBytes);
            if (exists == null || !exists) {
                result.setLeft(false);
                return null;
            }

            BlogData right = getBlog(connection, keyBytes);
            if (right == null) {
                result.setLeft(false);
                return null;
            }
            result.setRight(right);

            BlogData data = new BlogData();
            BeanUtils.copyProperties(blog, data, "content");
            byte[] bytes = ProtocstuffUtils.bean2Byte(data, BlogData.class);
            if (bytes == null) {
                result.setLeft(false);
                return null;
            }

            // TODO 和delete一样

            connection.set(keyBytes, bytes);
            connection.set(CONTENT.keyBytes(blog.getId()), blog.getContent());
            result.setLeft(true);
            return null;
        });

        if (!result.getLeft()) {
            return;
        }
        Blog document = new Blog();
        BeanUtils.copyProperties(blog, document, "content");
        document.setContent(new String(blog.getContent(), StandardCharsets.UTF_8));
        blogEsRepository.update(document);

        BlogData old = result.getRight();
        // TODO 将这两个操作合并到一起
        categoryRedisRepository.deleteThenAdd(old.getCategory(), blog.getCategory());
        tagRedisRepository.decrementThenIncrement(old.getTags(), blog.getTags());
    }

    @Override
    public PageResult<SimpleBlogVO> findAll(IPageRequest<BlogQueryVO> iPageRequest) {
        PageResult<SimpleBlogVO> result = PageResult.empty();

        redisTemplate.execute((RedisCallback<Object>) connection ->
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

    private BlogData getBlog(RedisConnection connection, byte[] keyBytes) {
        byte[] bytes = connection.get(keyBytes);
        if (bytes == null) {
            return null;
        }
        return ProtocstuffUtils.byte2Bean(bytes, BlogData.class);
    }
}
