package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.repository.CategoryRedisRepository;
import com.gapache.blog.server.dao.data.Category;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.gapache.blog.server.dao.data.Structures.CATEGORIES;

/**
 * @author HuSen
 * create on 2020/4/4 22:35
 */
@Slf4j
@Repository
public class CategoryRedisRepositoryImpl implements CategoryRedisRepository {

    private final StringRedisTemplate redisTemplate;

    public CategoryRedisRepositoryImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public List<Category> get() {
        return Optional
                .ofNullable(redisTemplate.opsForZSet().range(CATEGORIES.key(), 0, -1))
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
}
