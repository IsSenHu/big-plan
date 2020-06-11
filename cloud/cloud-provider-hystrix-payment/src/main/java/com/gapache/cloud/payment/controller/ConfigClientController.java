package com.gapache.cloud.payment.controller;

import com.gapache.commons.model.JsonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * curl -X POST "http://localhost:8009/actuator/refresh"
 * curl -X POST "http://localhost:3344/actuator/bus-refresh"
 * curl -X POST "http://localhost:3344/actuator/bus-refresh/cloud-provider-hystrix-payment:8009"
 *
 * @author HuSen
 * @since 2020/6/11 10:44 上午
 */
@RefreshScope
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
