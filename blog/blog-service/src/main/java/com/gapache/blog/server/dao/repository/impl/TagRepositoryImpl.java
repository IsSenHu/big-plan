package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.ro.Tag;
import com.gapache.blog.server.dao.repository.TagRepository;
import com.gapache.blog.server.lua.TagLuaScript;
import com.gapache.redis.RedisLuaExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/4/3 5:11 下午
 */
@Slf4j
@Repository
public class TagRepositoryImpl implements TagRepository {

    private final StringRedisTemplate template;
    private final RedisLuaExecutor luaExecutor;

    public TagRepositoryImpl(StringRedisTemplate template, RedisLuaExecutor luaExecutor) {
        this.template = template;
        this.luaExecutor = luaExecutor;
    }

    @Override
    public List<Tag> get() {
        return Optional
                .ofNullable(template.opsForZSet().reverseRangeWithScores("Blog:Tags", 0, -1))
                .map(x -> x
                        .stream()
                        .map(tuple ->
                        {
                            Tag tag = new Tag();
                            tag.setName(tuple.getValue());
                            tag.setCount(null != tuple.getScore() ? tuple.getScore().intValue() : 0);
                            return tag;
                        })
                        .collect(Collectors.toList()))
                .orElseGet(() ->
                {
                    log.error("get error was return null!");
                    return new ArrayList<>();
                });
    }

    @Override
    public void increment(String[] tags) {
        luaExecutor.execute(TagLuaScript.INCREMENT, Collections.singletonList("Blog:Tags"), String.join(",", tags));
    }

    @Override
    public void decrement(String[] tags) {
        luaExecutor.execute(TagLuaScript.DECREMENT, Collections.singletonList("Blog:Tags"), String.join(",", tags));
    }
}
