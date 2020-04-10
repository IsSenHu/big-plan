package com.gapache.blog.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.gapache.blog.sdk.dubbo.category.CategoryApiService;
import com.gapache.blog.sdk.dubbo.category.CategoryVO;
import com.gapache.blog.server.dao.repository.CategoryRedisRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/4/10 9:43 上午
 */
@Service
@Component
public class CategoryApiServiceImpl implements CategoryApiService {

    private final CategoryRedisRepository categoryRedisRepository;

    public CategoryApiServiceImpl(CategoryRedisRepository categoryRedisRepository) {
        this.categoryRedisRepository = categoryRedisRepository;
    }

    @Override
    public List<CategoryVO> findAll() {
        return categoryRedisRepository.get()
                .stream()
                .map(category -> new CategoryVO(category.getName()))
                .collect(Collectors.toList());
    }
}
