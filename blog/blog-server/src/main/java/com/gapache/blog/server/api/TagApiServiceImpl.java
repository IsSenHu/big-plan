package com.gapache.blog.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.gapache.blog.sdk.dubbo.tag.TagApiService;
import com.gapache.blog.sdk.dubbo.tag.TagVO;
import com.gapache.blog.server.dao.repository.TagRedisRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/4/10 9:46 上午
 */
@Service
@Component
public class TagApiServiceImpl implements TagApiService {

    private final TagRedisRepository tagRedisRepository;

    public TagApiServiceImpl(TagRedisRepository tagRedisRepository) {
        this.tagRedisRepository = tagRedisRepository;
    }

    @Override
    public List<TagVO> findAll() {
        return tagRedisRepository.get()
                .stream()
                .map(tag -> {
                    TagVO vo = new TagVO();
                    vo.setName(tag.getName());
                    vo.setCount(tag.getCount());
                    return vo;
                })
                .collect(Collectors.toList());
    }
}
