package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.data.Tag;
import com.gapache.blog.server.dao.repository.TagRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

import static com.gapache.blog.server.dao.data.Structures.TAGS;

/**
 * @author HuSen
 * create on 2020/4/3 5:11 下午
 */
@Slf4j
@Repository
public class TagRedisRepositoryImpl implements TagRedisRepository {

    private final StringRedisTemplate template;

    public TagRedisRepositoryImpl(StringRedisTemplate template) {
        this.template = template;
    }

    @Override
    public List<Tag> get() {
        return Optional
                .ofNullable(template.opsForZSet().reverseRangeWithScores(TAGS.key(), 0, -1))
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
}
