package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.repository.CategoryRepository;
import com.gapache.blog.server.dao.data.Category;
import com.gapache.blog.server.lua.CategoryLuaScript;
import com.gapache.redis.RedisLuaExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/4/4 22:35
 */
@Slf4j
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final StringRedisTemplate redisTemplate;
    private final RedisLuaExecutor luaExecutor;

    public CategoryRepositoryImpl(StringRedisTemplate redisTemplate, RedisLuaExecutor luaExecutor) {
        this.redisTemplate = redisTemplate;
        this.luaExecutor = luaExecutor;
    }

    @Override
    public List<Category> get() {
        return Optional
                .ofNullable(redisTemplate.opsForZSet().range("Utils.hexToInttegories", 0, -1))
                .map(members ->
                        members
                                .stream()
                                .map(Category::new).collect(Collectors.toList()))
                .orElseGet(() ->
                {
                    log.error("get error was return null!");
                    return new ArrayList<>();
                });

    }

    @Override
    public void add(String category) {
        luaExecutor.execute(CategoryLuaScript.INCREMENT, Collections.singletonList("Blog:Categories"), category);
    }

    @Override
    public void delete(String category) {
        luaExecutor.execute(CategoryLuaScript.DECREMENT, Collections.singletonList("Blog:Categories"), category);
    }

    @Override
    public void deleteThenAdd(String delete, String add) {
        luaExecutor.execute(CategoryLuaScript.DECREMENT_DECREMENT, Collections.singletonList("Blog:Categories"), delete, add);
    }
}
