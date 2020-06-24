package com.gapache.oms.store.location.sdk.model.error;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * @since 2020/6/24 5:27 下午
 */
public enum StoreLocationError implements Error {
    //
    GEOCODE_GEO_PARSE_FAIL(30001, "解析地址为经纬度失败");

    private Integer code;
    private String error;

    StoreLocationError(Integer code, String error) {
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
