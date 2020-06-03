package com.gapache.cloud.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author HuSen
 * @since 2020/6/3 4:16 下午
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final String PAYMENT = "http://cloud-payment-service";

    private final RestTemplate restTemplate;

    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping
    public String test() {
        return restTemplate.getForEntity(PAYMENT.concat("/api/payment"), String.class).getBody();
    }
}
