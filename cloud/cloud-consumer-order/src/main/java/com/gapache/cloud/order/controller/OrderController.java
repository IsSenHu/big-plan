package com.gapache.cloud.order.controller;

import com.gapache.cloud.sdk.PaymentVO;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author HuSen
 * create on 2020/6/3 00:10
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final String PAYMENT = "http://CLOUD-PAYMENT-SERVICE";

    private final RestTemplate restTemplate;

    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/payment/create")
    public JsonResult create(PaymentVO payment) {
        return restTemplate.postForEntity(PAYMENT.concat("/api/payment"), payment, JsonResult.class).getBody();
    }

    @GetMapping("/payment/get/{id}")
    public JsonResult get(@PathVariable Long id) {
        return restTemplate.getForEntity(PAYMENT.concat("/api/payment/".concat(id.toString())), JsonResult.class).getBody();
    }
}
