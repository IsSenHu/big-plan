package com.gapache.blog.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gapache.blog.sdk.dubbo.tag.TagApiService;
import com.gapache.blog.sdk.dubbo.tag.TagVO;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/10 9:53 上午
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {

    @Reference(check = false, version = "1.0.0")
    private TagApiService tagApiService;

    @GetMapping("/findAll")
    public JsonResult<List<TagVO>> findAll() {
        return JsonResult.of(tagApiService.findAll());
    }
}
