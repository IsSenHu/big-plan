package com.gapache.blog.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gapache.blog.admin.model.BlogError;
import com.gapache.blog.admin.model.vo.BlogCreateVO;
import com.gapache.blog.sdk.dubbo.blog.BlogApiService;
import com.gapache.blog.sdk.dubbo.blog.BlogVO;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
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

    @Reference(check = false)
    private BlogApiService blogApiService;

    @PutMapping
    public JsonResult<String> create(@RequestBody BlogCreateVO vo, MultipartFile file) {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);

        try (InputStream is = file.getInputStream(); ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int ch;
            while (-1 != (ch = is.read())) {
                byteArrayOutputStream.write(ch);
            }
            byte[] content = byteArrayOutputStream.toByteArray();

            BlogVO blogVO = new BlogVO();
            blogVO.setId(UUID.randomUUID().toString());
            blogVO.setTitle(vo.getTitle());
            blogVO.setIntroduction(vo.getIntroduction());
            blogVO.setContent(content);
            blogVO.setPublishTime(LocalDateTime.now());
            blogVO.setCategory(vo.getCategory());
            blogVO.setTags(vo.getTags().toArray(new String[0]));
            blogApiService.create(blogVO);

            return JsonResult.of(blogVO.getId());
        } catch (Exception e) {
            log.error("blog create error:{}.", vo, e);
            return JsonResult.of(BlogError.CREATE_ERROR);
        }
    }
}
