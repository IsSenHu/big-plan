package com.gapache.blog.server.service.impl;

import com.gapache.blog.server.dao.repository.CategoryRepository;
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

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public JsonResult<List<Category>> get() {
        List<Category> categories = categoryRepository.get();
        return JsonResult.of(categories);
    }
}
