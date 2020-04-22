package com.gapache.blog.server.controller;

import com.gapache.blog.server.model.vo.ArchiveVO;
import com.gapache.blog.server.model.vo.BlogSummaryVO;
import com.gapache.blog.server.model.vo.BlogVO;
import com.gapache.blog.server.model.vo.RankVO;
import com.gapache.blog.sdk.dubbo.blog.SimpleBlogVO;
import com.gapache.blog.server.service.BlogService;
import com.gapache.commons.model.JsonResult;
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

    public BlogController(BlogService blogService) {
        this.blogService = blogService;
    }

    @GetMapping("/{id}")
    public JsonResult<BlogVO> get(@PathVariable String id) {
        return blogService.get(id);
    }

    @GetMapping("/archive")
    public JsonResult<ArchiveVO> archive() {
        return blogService.archive();
    }

    @GetMapping("/views/{id}")
    public JsonResult<Object> views(@PathVariable String id) {
        return blogService.views(id);
    }

    @GetMapping("/top/{number}")
    public JsonResult<List<RankVO<SimpleBlogVO>>> top(@PathVariable Integer number) {
        return blogService.top(number);
    }

    @GetMapping("/findAllByCategory/{category}")
    public JsonResult<List<SimpleBlogVO>> findAllByCategory(@PathVariable String category) {
        return blogService.findAllByCategory(category);
    }

    @GetMapping("/findAllByTag/{tag}")
    public JsonResult<List<SimpleBlogVO>> findAllByTags(@PathVariable String tag) {
        return blogService.findAllByTags(tag);
    }

    @GetMapping("/search")
    public JsonResult<List<BlogSummaryVO>> search(@RequestParam(required = false) String queryString) {
        return blogService.search(queryString);
    }
}