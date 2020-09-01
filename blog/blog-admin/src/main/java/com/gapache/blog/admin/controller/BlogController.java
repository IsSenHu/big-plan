package com.gapache.blog.admin.controller;

import com.gapache.blog.admin.model.BlogError;
import com.gapache.blog.admin.model.dto.BlogCreateDTO;
import com.gapache.blog.admin.model.dto.BlogUpdateDTO;
import com.gapache.blog.common.util.IoUtils;
import com.gapache.blog.common.model.dto.BlogDTO;
import com.gapache.blog.common.model.dto.BlogQueryDTO;
import com.gapache.blog.common.model.dto.SimpleBlogDTO;
import com.gapache.blog.sdk.feign.BlogServerFeign;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
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

    @Resource
    private BlogServerFeign blogServerFeign;

    @PutMapping
    public JsonResult<Boolean> create(BlogCreateDTO blogCreateDTO, MultipartFile file) throws IOException {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);
        byte[] content = IoUtils.getContent(file.getInputStream());
        ThrowUtils.throwIfTrue(content == null, BlogError.CREATE_ERROR);
        BlogDTO dto = new BlogDTO();
        dto.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(blogCreateDTO, dto, "id", "content", "tags");
        dto.setTags(blogCreateDTO.getTags().split(","));
        dto.setContent(content);
        dto.setPublishTime(LocalDateTime.now());
        return blogServerFeign.create(dto);
    }

    @GetMapping("/get/{id}")
    public JsonResult<SimpleBlogDTO> get(@PathVariable String id) {
        return blogServerFeign.getSimple(id);
    }

    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable String id) {
        return blogServerFeign.delete(id);
    }

    @PostMapping
    public JsonResult<Boolean> update(BlogUpdateDTO blogUpdateDTO, MultipartFile file) throws IOException {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);

        byte[] content = IoUtils.getContent(file.getInputStream());
        ThrowUtils.throwIfTrue(content == null, BlogError.UPDATE_ERROR);

        BlogDTO dto = new BlogDTO();
        BeanUtils.copyProperties(blogUpdateDTO, dto, "content", "publishTime");
        dto.setTags(blogUpdateDTO.getTags().split(","));
        dto.setContent(content);
        dto.setPublishTime(TimeUtils.parse(TimeUtils.Format._2, blogUpdateDTO.getPublishTime()));
        return blogServerFeign.update(dto);
    }

    @PostMapping("/page")
    public JsonResult<PageResult<SimpleBlogDTO>> page(@RequestBody IPageRequest<BlogQueryDTO> iPageRequest) {
        return blogServerFeign.findAll(iPageRequest);
    }

    @GetMapping("/sync")
    public JsonResult<Boolean> sync(@RequestParam(required = false) String id) {
        return blogServerFeign.sync(id);
    }
}
