package com.gapache.cloud.payment.service;

import com.gapache.cloud.sdk.PaymentVO;

/**
 * @author HuSen
 * create on 2020/6/2 23:57
 */
public interface PaymentService {

    Long create(PaymentVO payment);

    PaymentVO findById(Long id);
}
