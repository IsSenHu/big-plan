package com.gapache.blog.server.api.impl;

import com.gapache.blog.common.model.dto.TagDTO;
import com.gapache.blog.server.api.TagApiService;
import com.gapache.blog.server.dao.repository.TagRedisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/4/10 9:46 上午
 */
@Service
public class TagApiServiceImpl implements TagApiService {

    private final TagRedisRepository tagRedisRepository;

    public TagApiServiceImpl(TagRedisRepository tagRedisRepository) {
        this.tagRedisRepository = tagRedisRepository;
    }

    @Override
    public List<TagDTO> findAll() {
        return tagRedisRepository.get()
                .stream()
                .map(tag -> {
                    TagDTO vo = new TagDTO();
                    vo.setName(tag.getName());
                    vo.setCount(tag.getCount());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
