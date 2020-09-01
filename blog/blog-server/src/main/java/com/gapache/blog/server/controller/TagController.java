package com.gapache.blog.server.controller;

import com.gapache.blog.common.model.dto.TagDTO;
import com.gapache.blog.server.api.TagApiService;
import com.gapache.blog.server.dao.data.Tag;
import com.gapache.blog.server.service.TagService;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/3 5:12 下午
 */
@RestController
@RequestMapping("/api/tag")
public class TagController {

    private final TagService tagService;
    private final TagApiService tagApiService;

    public TagController(TagService tagService, TagApiService tagApiService) {
        this.tagService = tagService;
        this.tagApiService = tagApiService;
    }

    @GetMapping("/findAll")
    public JsonResult<List<TagDTO>> findAll() {
        return JsonResult.of(tagApiService.findAll());
    }

    @GetMapping
    public JsonResult<List<Tag>> get() {
        return tagService.get();
    }
}
