package com.gapache.blog.server.service.impl;

import com.gapache.blog.server.dao.repository.CategoryRedisRepository;
import com.gapache.blog.server.dao.data.Category;
import com.gapache.blog.server.service.CategoryService;
import com.gapache.commons.model.JsonResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/4 22:34
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRedisRepository categoryRedisRepository;

    public CategoryServiceImpl(CategoryRedisRepository categoryRedisRepository) {
        this.categoryRedisRepository = categoryRedisRepository;
    }

    @Override
    public JsonResult<List<Category>> get() {
        List<Category> categories = categoryRedisRepository.get();
        return JsonResult.of(categories);
    }
}
