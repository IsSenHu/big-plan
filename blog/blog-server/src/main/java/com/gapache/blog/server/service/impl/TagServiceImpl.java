package com.gapache.blog.server.service.impl;

import com.gapache.blog.server.dao.data.Tag;
import com.gapache.blog.server.dao.repository.TagRedisRepository;
import com.gapache.blog.server.service.TagService;
import com.gapache.commons.model.JsonResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/3 5:12 下午
 */
@Service
public class TagServiceImpl implements TagService {

    private final TagRedisRepository tagRedisRepository;

    public TagServiceImpl(TagRedisRepository tagRedisRepository) {
        this.tagRedisRepository = tagRedisRepository;
    }

    @Override
    public JsonResult<List<Tag>> get() {
        return JsonResult.of(tagRedisRepository.get());
    }
}
