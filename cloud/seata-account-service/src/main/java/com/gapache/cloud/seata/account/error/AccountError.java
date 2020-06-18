package com.gapache.cloud.seata.account.error;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * @since 2020/6/18 2:41 下午
 */
public enum AccountError implements Error {
    //
    INSUFFICIENT_BALANCE(10001, "余额不足");

    private Integer code;
    private String error;

    AccountError(Integer code, String error) {
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
