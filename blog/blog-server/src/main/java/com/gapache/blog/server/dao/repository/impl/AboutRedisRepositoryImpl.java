package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.repository.AboutRedisRepository;
import com.gapache.blog.server.dao.data.About;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import static com.gapache.blog.server.dao.data.Structures.ABOUT;

/**
 * @author HuSen
 * create on 2020/4/5 20:23
 */
@Repository
public class AboutRedisRepositoryImpl implements AboutRedisRepository {

    private final StringRedisTemplate redisTemplate;

    public AboutRedisRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public About get() {
        return redisTemplate.execute((RedisCallback<About>) connection ->
        {
            byte[] bytes = connection.get(ABOUT.keyBytes());
            return ProtocstuffUtils.byte2Bean(bytes, About.class);
        });
    }

    @Override
    public void save(About about) {
        redisTemplate.execute((RedisCallback<Object>) connection ->
        {
            byte[] bytes = ProtocstuffUtils.bean2Byte(about, About.class);
            if (bytes != null) {
                connection.set(ABOUT.keyBytes(), bytes);
            }
            return null;
        });
    }
}
