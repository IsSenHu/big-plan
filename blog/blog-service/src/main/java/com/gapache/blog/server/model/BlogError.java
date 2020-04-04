package com.gapache.blog.server.model;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * create on 2020/4/3 2:08 下午
 */
public enum BlogError implements Error {
    //
    NOT_FOUND(60001, "没有找到对应的博客!"),
    OP_ERROR(60002, "删除失败!");

    private Integer code;
    private String error;

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
