package com.gapache.oms.order.base.model.error;

import com.gapache.commons.model.Error;

public enum ServerCommonError implements Error {
    FALLBACK(5000, "服务降级");

    private final Integer code;
    private final String error;

    ServerCommonError(Integer code, String error) {
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
