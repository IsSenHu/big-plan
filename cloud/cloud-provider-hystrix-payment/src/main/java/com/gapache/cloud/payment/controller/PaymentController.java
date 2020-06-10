package com.gapache.cloud.payment.controller;

import com.gapache.cloud.payment.service.PaymentService;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @since 2020/6/10 9:53 上午
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/randomInteger")
    public JsonResult<String> randomInteger() {
        return paymentService.randomInteger();
    }

    @GetMapping("/randomIntegerError")
    public JsonResult<String> randomIntegerError() {
        return paymentService.randomIntegerError();
    }

    @GetMapping("/randomIntegerCircuitBreaker/{id}")
    public JsonResult<String> randomIntegerCircuitBreaker(@PathVariable("id") int id) {
        return paymentService.randomIntegerCircuitBreaker(id);
    }
}
