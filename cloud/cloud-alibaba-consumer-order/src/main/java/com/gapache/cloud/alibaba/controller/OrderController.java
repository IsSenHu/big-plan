package com.gapache.cloud.alibaba.controller;

import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/12 3:08 下午
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final String PAYMENT = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;

    @GetMapping("/getPort")
    public JsonResult<String> getPort() {
        String body = restTemplate.getForEntity(PAYMENT.concat("/api/payment/getPort"), String.class).getBody();
        return JsonResult.of(body);
    }
}
