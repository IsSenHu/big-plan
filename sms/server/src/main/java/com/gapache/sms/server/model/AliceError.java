package com.gapache.sms.server.model;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * create on 2020/1/14 11:59
 */
public enum AliceError implements Error {

    REQUEST_EXCEPTION(20001, "调用短信API发生异常"),
    REQUEST_FAIL(20002, "调用短信API失败");

    private Integer code;
    private String error;

    AliceError(Integer code, String error) {
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
