package com.gapache.blog.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gapache.blog.sdk.dubbo.category.CategoryApiService;
import com.gapache.blog.sdk.dubbo.category.CategoryVO;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/10 9:55 上午
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Reference(check = false, version = "1.0.0")
    private CategoryApiService categoryApiService;

    @GetMapping("/findAll")
    public JsonResult<List<CategoryVO>> findAll() {
        return JsonResult.of(categoryApiService.findAll());
    }
}
