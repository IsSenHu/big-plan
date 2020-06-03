package com.gapache.cloud.payment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @author HuSen
 * @since 2020/6/3 3:35 下午
 */
@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @GetMapping
    public String test() {
        return UUID.randomUUID().toString();
    }
}
