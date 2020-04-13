package com.gapache.blog.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gapache.blog.admin.model.BlogError;
import com.gapache.blog.admin.model.vo.BlogCreateVO;
import com.gapache.blog.admin.model.vo.BlogUpdateVO;
import com.gapache.blog.sdk.dubbo.blog.BlogApiService;
import com.gapache.blog.sdk.dubbo.blog.BlogQueryVO;
import com.gapache.blog.sdk.dubbo.blog.BlogVO;
import com.gapache.blog.sdk.dubbo.blog.SimpleBlogVO;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.blog.admin.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author HuSen
 * create on 2020/4/5 03:42
 */
@Slf4j
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Reference(version = "1.0.0")
    private BlogApiService blogApiService;

    @PutMapping
    public JsonResult<Boolean> create(BlogCreateVO vo, MultipartFile file) {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);

        byte[] content = IOUtils.getContent(file);
        ThrowUtils.throwIfTrue(content == null, BlogError.CREATE_ERROR);

        BlogVO blogVO = new BlogVO();
        blogVO.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(vo, blogVO, "id", "content", "tags");
        blogVO.setTags(vo.getTags().split(","));
        blogVO.setContent(content);
        blogVO.setPublishTime(LocalDateTime.now());

        return JsonResult.of(blogApiService.create(blogVO));
    }

    @GetMapping("/get/{id}")
    public JsonResult<SimpleBlogVO> get(@PathVariable String id) {
        SimpleBlogVO vo = blogApiService.get(id);
        ThrowUtils.throwIfTrue(vo == null, BlogError.NOT_FOUNT);
        return JsonResult.of(vo);
    }

    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable String id) {
        return JsonResult.of(blogApiService.delete(id));
    }

    @PostMapping
    public JsonResult<Boolean> update(BlogUpdateVO vo, MultipartFile file) {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);

        byte[] content = IOUtils.getContent(file);
        ThrowUtils.throwIfTrue(content == null, BlogError.UPDATE_ERROR);

        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(vo, blogVO, "content");
        blogVO.setTags(vo.getTags().split(","));
        blogVO.setContent(content);

        return JsonResult.of(blogApiService.update(blogVO));
    }

    @PostMapping("/page")
    public JsonResult<PageResult<SimpleBlogVO>> page(@RequestBody IPageRequest<BlogQueryVO> iPageRequest) {
        return JsonResult.of(blogApiService.findAll(iPageRequest));
    }

    @GetMapping("/sync")
    public JsonResult<Object> sync(@RequestParam(required = false) String id) {
        blogApiService.sync(id);
        return JsonResult.success();
    }
}
