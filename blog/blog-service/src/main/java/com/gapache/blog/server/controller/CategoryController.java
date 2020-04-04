package com.gapache.blog.server.controller;

import com.gapache.blog.server.dao.ro.Category;
import com.gapache.blog.server.service.CategoryService;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/4 22:32
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public JsonResult<List<Category>> get() {
        return categoryService.get();
    }
}
