package com.gapache.blog.admin.controller;

import com.gapache.blog.common.model.dto.TagDTO;
import com.gapache.blog.sdk.feign.BlogServerFeign;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/10 9:53 上午
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Resource
    private BlogServerFeign blogServerFeign;

    @GetMapping("/findAll")
    public JsonResult<List<TagDTO>> findAll() {
        return blogServerFeign.findAllTag();
    }
}
