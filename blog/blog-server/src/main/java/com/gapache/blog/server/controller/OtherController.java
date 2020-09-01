package com.gapache.blog.server.controller;

import com.aliyun.oss.model.Bucket;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.StorageClass;
import com.gapache.blog.common.model.dto.FriendlyLinkDTO;
import com.gapache.blog.common.model.error.BlogError;
import com.gapache.blog.server.service.OtherService;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.oss.OssTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/28 9:28 上午
 */
@Slf4j
@RestController
@RequestMapping("/api/hussen")
public class OtherController {

    private static final String MY_INFO = "MyInfo";
    private final OtherService otherService;
    private final OssTemplate ossTemplate;

    public OtherController(OtherService otherService, OssTemplate ossTemplate) {
        this.otherService = otherService;
        this.ossTemplate = ossTemplate;
    }

    @GetMapping("/wxQrCode")
    public JsonResult<String> wxQrCode() {
        return otherService.wxQrCode();
    }

    @PostMapping("/saveWxQrCode")
    public JsonResult<Boolean> saveWxQrCode(MultipartFile file) throws IOException {
        ThrowUtils.throwIfTrue(file == null, BlogError.FILE_IS_NULL);

        boolean flag = false;
        boolean doesBucketExist = ossTemplate.doesBucketExist(MY_INFO);
        if (!doesBucketExist) {
            Bucket bucket = ossTemplate.createBucket(MY_INFO, CannedAccessControlList.PublicRead, StorageClass.Standard);
            log.info("创建Bucket:{}", bucket);
            flag = null != bucket;
        }

        if (!flag) {
            return JsonResult.of(false);
        }

        String originalFilename = file.getOriginalFilename();
        String fullObjectName = "/wx/" + originalFilename;
        byte[] data = FileCopyUtils.copyToByteArray(file.getInputStream());
        boolean result = ossTemplate.uploadFile(MY_INFO, fullObjectName, data);
        if (!result) {
            return JsonResult.of(false);
        }

        String url = ossTemplate.generatePublicUrl(MY_INFO, fullObjectName);
        return otherService.saveWxQrCode(url);
    }

    @GetMapping("/friendlyLinks")
    public JsonResult<List<FriendlyLinkDTO>> friendlyLinks() {
        return otherService.friendlyLinks();
    }

    @PostMapping("/saveFriendlyLinks")
    public JsonResult<Boolean> saveFriendlyLinks(@RequestBody List<FriendlyLinkDTO> list) {
        return otherService.saveFriendlyLinks(list);
    }
}
