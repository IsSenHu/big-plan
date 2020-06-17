package com.gapache.cloud.seata.storage.controller;

import com.gapache.cloud.seata.storage.service.StorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 6:20 下午
 */
@RestController
@RequestMapping("/api/storage")
public class StorageController {

    @Resource
    private StorageService storageService;

    @PostMapping("/deduct")
    public void deduct(String commodityCode, int count) {
        storageService.deduct(commodityCode, count);
    }
}
