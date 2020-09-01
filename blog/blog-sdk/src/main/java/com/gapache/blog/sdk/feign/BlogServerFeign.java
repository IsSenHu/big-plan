package com.gapache.blog.sdk.feign;

import com.gapache.blog.common.model.dto.*;
import com.gapache.blog.sdk.fallback.BlogServerFeignFallback;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/26 9:33 上午
 */
@FeignClient(value = "blog-server", path = "/api", fallback = BlogServerFeignFallback.class)
public interface BlogServerFeign {

    /* about */

    @PutMapping("/about")
    JsonResult<Boolean> saveAbout(AboutDTO dto);

    /* category */

    @GetMapping("/category/findAll")
    JsonResult<List<CategoryDTO>> findAllCategory();

    /* tag */

    @GetMapping("/tag/findAll")
    JsonResult<List<TagDTO>> findAllTag();

    /* blog */

    @PutMapping("/blog")
    JsonResult<Boolean> create(@RequestBody BlogDTO blog);

    @GetMapping("/blog/getSimple/{id}")
    JsonResult<SimpleBlogDTO> getSimple(@PathVariable("id") String id);

    @DeleteMapping("/blog/{id}")
    JsonResult<Boolean> delete(@PathVariable("id") String id);

    @PostMapping("/blog")
    JsonResult<Boolean> update(@RequestBody BlogDTO blog);

    @PostMapping("/blog/page")
    JsonResult<PageResult<SimpleBlogDTO>> findAll(@RequestBody IPageRequest<BlogQueryDTO> iPageRequest);

    @PostMapping("/blog/sync/{id}")
    JsonResult<Boolean> sync(@PathVariable("id") String id);
}
