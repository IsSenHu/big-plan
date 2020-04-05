package com.gapache.blog.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gapache.blog.admin.model.BlogError;
import com.gapache.blog.admin.model.vo.BlogCreateVO;
import com.gapache.blog.admin.model.vo.BlogUpdateVO;
import com.gapache.blog.sdk.dubbo.blog.BlogApiService;
import com.gapache.blog.sdk.dubbo.blog.BlogVO;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.blog.admin.utils.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author HuSen
 * create on 2020/4/5 03:42
 */
@Slf4j
@RestController
@RequestMapping("/api/blog")
public class BlogController {

    @Reference(check = false)
    private BlogApiService blogApiService;

    @PutMapping
    public JsonResult<String> create(@RequestBody BlogCreateVO vo, MultipartFile file) {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);

        byte[] content = IOUtils.getContent(file);
        ThrowUtils.throwIfTrue(content == null, BlogError.CREATE_ERROR);

        BlogVO blogVO = new BlogVO();
        blogVO.setId(UUID.randomUUID().toString());
        BeanUtils.copyProperties(vo, blogVO, "id", "content");
        blogVO.setContent(content);

        blogApiService.create(blogVO);

        return JsonResult.of(blogVO.getId());
    }

    @DeleteMapping("/{id}")
    public JsonResult<Boolean> delete(@PathVariable String id) {
        return JsonResult.of(blogApiService.delete(id));
    }

    @PostMapping
    public JsonResult<String> update(@RequestBody BlogUpdateVO vo, MultipartFile file) {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);

        byte[] content = IOUtils.getContent(file);
        ThrowUtils.throwIfTrue(content == null, BlogError.UPDATE_ERROR);

        BlogVO blogVO = new BlogVO();
        BeanUtils.copyProperties(vo, blogVO, "content");
        blogVO.setContent(content);

        blogApiService.update(blogVO);

        return JsonResult.of(vo.getId());
    }
}
