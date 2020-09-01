package com.gapache.blog.server.controller;

import com.gapache.blog.common.model.dto.AboutDTO;
import com.gapache.blog.server.api.AboutApiService;
import com.gapache.blog.server.service.AboutService;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * create on 2020/4/6 11:48
 */
@RestController
@RequestMapping("/api/about")
public class AboutController {

    private final AboutService aboutService;
    private final AboutApiService aboutApiService;

    public AboutController(AboutService aboutService, AboutApiService aboutApiService) {
        this.aboutService = aboutService;
        this.aboutApiService = aboutApiService;
    }

    @PutMapping
    public JsonResult<Boolean> save(AboutDTO dto) {
        aboutApiService.save(dto);
        return JsonResult.success();
    }

    @GetMapping
    public JsonResult<String> get() {
        return aboutService.get();
    }
}
