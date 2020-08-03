package com.gapache.commons.model;

import lombok.Getter;

/**
 * @author HuSen
 * @since 2020/8/3 10:21 上午
 */
@Getter
public enum SystemError implements Error {
    //
    SERVER_EXCEPTION(999999, "system error");

    private Integer code;
    private String error;

    SystemError(Integer code, String error) {
        this.code = code;
        this.error = error;
    }
}
