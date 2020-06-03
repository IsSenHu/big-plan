package com.gapache.cloud.payment.error;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * create on 2020/6/3 00:07
 */
public enum PaymentError implements Error {
    //
    NOT_FOUND(70001, "没有该支付信息");

    private Integer code;
    private String error;

    PaymentError(Integer code, String error) {
        this.code = code;
        this.error = error;
    }

    @Override
    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getError() {
        return this.error;
    }
}
