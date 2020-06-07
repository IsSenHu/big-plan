package com.gapache.cloud.order.controller;

import com.gapache.cloud.order.feign.PaymentFeign;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * create on 2020/6/8 00:33
 */
@RestController
@RequestMapping("/api/consumer")
public class ConsumerController {

    private final PaymentFeign paymentFeign;

    public ConsumerController(PaymentFeign paymentFeign) {
        this.paymentFeign = paymentFeign;
    }

    @GetMapping("/getPortTimeout")
    public JsonResult<Integer> getPortTimeout() {
        return JsonResult.of(paymentFeign.getPortTimeout());
    }
}
