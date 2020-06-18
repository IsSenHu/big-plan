package com.gapache.cloud.seata.storage.error;

import com.gapache.commons.model.Error;

/**
 * @author HuSen
 * @since 2020/6/18 2:41 下午
 */
public enum StorageError implements Error {
    //
    INVENTORY_BALANCE(20001, "库存不足");

    private Integer code;
    private String error;

    StorageError(Integer code, String error) {
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
