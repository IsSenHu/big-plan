package com.gapache.blog.admin.controller;

import com.gapache.blog.common.model.dto.CategoryDTO;
import com.gapache.blog.sdk.feign.BlogServerFeign;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/10 9:55 上午
 */
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Resource
    private BlogServerFeign blogServerFeign;

    @GetMapping("/findAll")
    public JsonResult<List<CategoryDTO>> findAll() {
        return blogServerFeign.findAllCategory();
    }
}
