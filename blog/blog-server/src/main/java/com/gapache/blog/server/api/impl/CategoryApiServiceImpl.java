package com.gapache.blog.server.api.impl;

import com.gapache.blog.common.model.dto.CategoryDTO;
import com.gapache.blog.server.api.CategoryApiService;
import com.gapache.blog.server.dao.repository.CategoryRedisRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/4/10 9:43 上午
 */
@Service
public class CategoryApiServiceImpl implements CategoryApiService {

    private final CategoryRedisRepository categoryRedisRepository;

    public CategoryApiServiceImpl(CategoryRedisRepository categoryRedisRepository) {
        this.categoryRedisRepository = categoryRedisRepository;
    }

    @Override
    public List<CategoryDTO> findAll() {
        return categoryRedisRepository.get()
                .stream()
                .map(category -> new CategoryDTO(category.getName()))
                .collect(Collectors.toList());
    }
}
