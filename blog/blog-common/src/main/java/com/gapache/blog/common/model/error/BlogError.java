package com.gapache.blog.common.model.error;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * create on 2020/4/3 2:08 下午
 */
public enum BlogError implements Error {
    //
    NOT_FOUND(30001, "没有找到对应的博客!"),
    VERIFY_TOURIST_FAIL(30002, "账号不存在或者密码错误"),
    EXISTS_BY_EMAIL(30003, "邮箱已被注册"),
    EXISTS_BY_NICK(30004, "昵称已被使用"),
    NOT_LOGIN(30005, "没有登陆"),
    FILE_IS_NULL(30006, "文件为空");

    private final Integer code;
    private final String error;

    BlogError(Integer code, String error) {
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
