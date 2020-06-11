package com.gapache.cloud.payment.controller;

import com.gapache.commons.model.JsonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @since 2020/6/11 10:44 上午
 */
@RestController
@RequestMapping("/api/config")
public class ConfigClientController {

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/configInfo")
    public JsonResult<String> getConfigInfo() {
        return JsonResult.of(configInfo);
    }
}