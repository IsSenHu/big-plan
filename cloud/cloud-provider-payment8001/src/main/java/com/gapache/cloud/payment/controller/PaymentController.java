package com.gapache.cloud.payment.controller;

import com.gapache.cloud.payment.service.PaymentService;
import com.gapache.cloud.sdk.PaymentVO;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.*;

/**
 * @author HuSen
 * @since 2020/6/2 5:13 下午
 */
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public JsonResult<Long> create(@RequestBody PaymentVO payment) {
        return JsonResult.of(paymentService.create(payment));
    }

    @GetMapping("/{id}")
    public JsonResult<PaymentVO> get(@PathVariable("id") Long id) {
        return JsonResult.of(paymentService.findById(id));
    }
}
