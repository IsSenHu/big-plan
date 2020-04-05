package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.repository.AboutRepository;
import com.gapache.blog.server.dao.data.About;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.commons.utils.TimeUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

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
    public void save(About about) {
        redisTemplate.execute((RedisCallback<Object>) connection ->
        {
            final byte[] keyBytes = IStringUtils.getBytes("Blog:About");
            Map<byte[], byte[]> hashes = new HashMap<>(2);
            hashes.put(IStringUtils.getBytes("content"), about.getContent());
            hashes.put(IStringUtils.getBytes("lastOpTime"), IStringUtils.getBytes(TimeUtils.format(TimeUtils.Format._3, about.getLastOpTime())));

            connection.hMSet(keyBytes, hashes);
            return null;
        });
    }
}
