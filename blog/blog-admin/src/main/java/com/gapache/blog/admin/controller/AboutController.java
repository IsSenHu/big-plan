package com.gapache.blog.admin.controller;

import com.gapache.blog.admin.model.BlogError;
import com.gapache.blog.common.util.IoUtils;
import com.gapache.blog.common.model.dto.AboutDTO;
import com.gapache.blog.sdk.feign.BlogServerFeign;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/5 20:24
 */
@RestController
@RequestMapping("/api/about")
public class AboutController {

    @Resource
    private BlogServerFeign blogServerFeign;

    @PutMapping
    public JsonResult<Boolean> save(MultipartFile file) throws IOException {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);

        byte[] content = IoUtils.getContent(file.getInputStream());
        ThrowUtils.throwIfTrue(content == null, BlogError.CREATE_ERROR);

        AboutDTO dto = new AboutDTO();
        dto.setContent(content);
        dto.setLastOpTime(LocalDateTime.now());
        return blogServerFeign.saveAbout(dto);
    }
}
