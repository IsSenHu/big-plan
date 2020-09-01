package com.gapache.blog.server.controller;

import com.gapache.blog.common.model.dto.*;
import com.gapache.blog.server.api.BlogApiService;
import com.gapache.blog.server.service.BlogService;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/3 1:14 下午
 */
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    private final BlogService blogService;
    private final BlogApiService blogApiService;

    public BlogController(BlogService blogService, BlogApiService blogApiService) {
        this.blogService = blogService;
        this.blogApiService = blogApiService;
    }

    @GetMapping("/{id}")
    public JsonResult<FullBlogDTO> get(@PathVariable String id) {
        return blogService.get(id);
    }

    @GetMapping("/archive")
    public JsonResult<ArchiveDTO> archive() {
        return blogService.archive();
    }

    @GetMapping("/views/{id}")
    public JsonResult<Object> views(@PathVariable String id) {
        return blogService.views(id);
    }

    @GetMapping("/top/{from}/{number}")
    public JsonResult<List<RankDTO<SimpleBlogDTO>>> top(@PathVariable Integer from, @PathVariable Integer number) {
        return blogService.top(from, number);
    }

    @GetMapping("/find")
    public JsonResult<List<RankDTO<SimpleBlogDTO>>> find(@RequestBody IPageRequest<BlogQueryDTO> iPageRequest) {
        return blogService.find(iPageRequest);
    }

    @GetMapping("/findAllByCategory/{category}")
    public JsonResult<List<SimpleBlogDTO>> findAllByCategory(@PathVariable String category) {
        return blogService.findAllByCategory(category);
    }

    @GetMapping("/findAllByTag/{tag}")
    public JsonResult<List<SimpleBlogDTO>> findAllByTags(@PathVariable String tag) {
        return blogService.findAllByTags(tag);
    }

    @GetMapping("/search")
    public JsonResult<List<BlogSummaryDTO>> search(@RequestParam(required = false) String queryString) {
        return blogService.search(queryString);
    }

    @PutMapping
    public JsonResult<Boolean> create(@RequestBody BlogDTO blog) {
        return JsonResult.of(blogApiService.create(blog));
    }

    @GetMapping("/getNewest/{number}")
    public JsonResult<List<SimpleBlogDTO>> getNewest(@PathVariable Integer number) {
        return blogService.getNewest(number);
    }

    @GetMapping("/getSimple/{id}")
    public JsonResult<SimpleBlogDTO> getSimple(@PathVariable("id") String id) {
        return JsonResult.of(blogApiService.get(id));
    }

    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable("id") String id) {
        return JsonResult.of(blogApiService.delete(id));
    }

    @PostMapping
    public JsonResult<Boolean> update(@RequestBody BlogDTO blog) {
        return JsonResult.of(blogApiService.update(blog));
    }

    @PostMapping("/page")
    public JsonResult<PageResult<SimpleBlogDTO>> findAll(@RequestBody IPageRequest<BlogQueryDTO> iPageRequest) {
        return JsonResult.of(blogApiService.findAll(iPageRequest));
    }

    @PostMapping("/sync/{id}")
    public JsonResult<Boolean> sync(@PathVariable("id") String id) {
        blogApiService.sync(id);
        return JsonResult.success();
    }
}
