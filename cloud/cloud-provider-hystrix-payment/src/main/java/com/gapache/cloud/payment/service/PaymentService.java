package com.gapache.cloud.payment.service;

import com.gapache.commons.model.JsonResult;

/**
 * @author HuSen
 * @since 2020/6/10 9:53 上午
 */
public interface PaymentService {

    JsonResult<String> randomInteger();

    JsonResult<String> randomIntegerError();

    JsonResult<String> randomIntegerCircuitBreaker(int id);
}
