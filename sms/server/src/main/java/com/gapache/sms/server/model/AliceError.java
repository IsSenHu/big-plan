package com.gapache.sms.server.model;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * create on 2020/1/14 11:59
 */
public enum AliceError implements Error {

    REQUEST_EXCEPTION(20001, "调用短信API发生异常"),
    REQUEST_FAIL(20002, "调用短信API失败"),
    SIGN_NOT_EXISTED(20003, "签名不存在"),
    TEMPLATE_NOT_EXISTED(20004, "模版不存在"),
    NOT_HAVE_CODE(20005, "验证码类型短信没有验证码参数"),
    CODE_ALWAYS_SINGLE(20006, "验证码总是单独发送"),
    SEND_FREQUENTLY(20007, "短信发送频繁");

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
