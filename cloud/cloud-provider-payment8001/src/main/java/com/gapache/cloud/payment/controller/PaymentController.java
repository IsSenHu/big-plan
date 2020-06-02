package com.gapache.cloud.payment.controller;

import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @since 2020/6/2 5:13 下午
 */
@RestController
public class PaymentController {

    @PostMapping
    public JsonResult<Long> create() {
        return null;
    }
}
