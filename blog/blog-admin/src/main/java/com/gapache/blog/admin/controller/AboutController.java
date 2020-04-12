package com.gapache.blog.admin.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gapache.blog.admin.model.BlogError;
import com.gapache.blog.admin.utils.IOUtils;
import com.gapache.blog.sdk.dubbo.about.AboutApiService;
import com.gapache.blog.sdk.dubbo.about.AboutVO;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/5 20:24
 */
@RestController
@RequestMapping("/api/about")
public class AboutController {

    @Reference(version = "1.0.0")
    private AboutApiService aboutApiService;

    @PutMapping
    public JsonResult<Object> save(MultipartFile file) {
        ThrowUtils.throwIfTrue(file == null || StringUtils.isBlank(file.getOriginalFilename()), BlogError.FILE_IS_NULL);

        byte[] content = IOUtils.getContent(file);
        ThrowUtils.throwIfTrue(content == null, BlogError.CREATE_ERROR);

        AboutVO vo = new AboutVO();
        vo.setContent(content);
        vo.setLastOpTime(LocalDateTime.now());
        aboutApiService.save(vo);

        return JsonResult.success();
    }
}
