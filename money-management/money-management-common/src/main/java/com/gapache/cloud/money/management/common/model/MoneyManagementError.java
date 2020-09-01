package com.gapache.cloud.money.management.common.model;

import com.gapache.commons.model.Error;
import lombok.Getter;

/**
 * @author HuSen
 * @since 2020/8/13 5:16 下午
 */
@Getter
public enum MoneyManagementError implements Error {
    //
    FILE_EMPTY(20001, "文件为空");
    private Integer code;
    private String error;

    MoneyManagementError(Integer code, String error) {
        this.code = code;
        this.error = error;
    }
}
