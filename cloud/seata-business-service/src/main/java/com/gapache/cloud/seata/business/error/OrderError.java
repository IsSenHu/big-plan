package com.gapache.cloud.seata.business.error;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * @since 2020/6/18 2:41 下午
 */
public enum OrderError implements Error {
    //
    CREATE_FAIL(30001, "创建失败");

    private Integer code;
    private String error;

    OrderError(Integer code, String error) {
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
