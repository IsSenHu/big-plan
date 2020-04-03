package com.gapache.blog.server.controller;

import com.gapache.blog.server.dao.document.Tag;
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

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public JsonResult<List<Tag>> get() {
        return tagService.get();
    }
}
