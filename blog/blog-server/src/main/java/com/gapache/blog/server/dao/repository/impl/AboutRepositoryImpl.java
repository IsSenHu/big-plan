package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.repository.AboutRepository;
import com.gapache.blog.server.dao.data.About;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen
 * create on 2020/4/5 20:23
 */
@Repository
public class AboutRepositoryImpl implements AboutRepository {

    private final StringRedisTemplate redisTemplate;

    public AboutRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public About get() {
        return redisTemplate.execute((RedisCallback<About>) connection ->
        {
            final byte[] keyBytes = IStringUtils.getBytes("Blog:About");
            byte[] bytes = connection.get(keyBytes);
            return ProtocstuffUtils.byte2Bean(bytes, About.class);
        });
    }

    @Override
    public void save(About about) {
        redisTemplate.execute((RedisCallback<Object>) connection ->
        {
            final byte[] keyBytes = IStringUtils.getBytes("Blog:About");
            byte[] bytes = ProtocstuffUtils.bean2Byte(about, About.class);
            if (bytes != null) {
                connection.set(keyBytes, bytes);
            }
            return null;
        });
    }
}
