package com.gapache.cloud.alibaba.controller;

import com.gapache.commons.model.JsonResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @since 2020/6/12 3:16 下午
 */
@RefreshScope
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${server.port}")
    private String port;

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/getPort")
    public JsonResult<String> getPort() {
        return JsonResult.of(this.port);
    }

    @GetMapping("/getConfigInfo")
    public String getConfigInfo() {
        return this.configInfo;
    }
}
