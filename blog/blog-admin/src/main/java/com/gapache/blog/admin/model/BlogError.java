package com.gapache.blog.admin.model;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * create on 2020/4/5 03:43
 */
public enum BlogError implements Error {
    //
    FILE_IS_NULL(60001, "文件不能为空!"),
    CREATE_ERROR(60002, "创建博客失败!");

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
