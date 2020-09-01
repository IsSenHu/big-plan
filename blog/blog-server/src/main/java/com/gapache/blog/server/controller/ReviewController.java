package com.gapache.blog.server.controller;

import com.gapache.blog.common.model.dto.ReviewDTO;
import com.gapache.blog.server.service.ReviewService;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author HuSen
 * @since 2020/8/28 9:27 上午
 */
@RestController
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public JsonResult<Boolean> create(HttpServletRequest request, @RequestBody ReviewDTO dto) {
        return reviewService.create(getTouristToken(request), dto);
    }

    private String getTouristToken(HttpServletRequest request) {
        return request.getHeader("tourist-token");
    }
}
