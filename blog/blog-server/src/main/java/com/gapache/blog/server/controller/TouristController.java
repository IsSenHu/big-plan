package com.gapache.blog.server.controller;

import com.gapache.blog.common.model.dto.TouristDTO;
import com.gapache.blog.server.service.TouristService;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @since 2020/8/28 1:30 下午
 */
@RestController
@RequestMapping("/api/tourist")
public class TouristController {

    private final TouristService touristService;

    public TouristController(TouristService touristService) {
        this.touristService = touristService;
    }

    @PostMapping("/login")
    public JsonResult<String> login(@RequestBody TouristDTO dto) {
        return touristService.login(dto);
    }
}
