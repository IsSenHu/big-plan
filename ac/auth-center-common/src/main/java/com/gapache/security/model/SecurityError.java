package com.gapache.security.model;

import com.gapache.commons.model.Error;
import lombok.Getter;

/**
 * @author HuSen
 * @since 2020/7/31 2:22 下午
 */
@Getter
public enum SecurityError implements Error {
    //
    LOGIN_FAIL(10403, "username or password wrong"),
    ERROR_CODE(10503, "mode of authorization_code's code invalid"),
    CLIENT_ERROR(10504, "client auth error"),
    NOT_SUPPORT(10505, "not support");

    SecurityError(Integer code, String error) {
        this.code = code;
        this.error = error;
    }

    private final Integer code;
    private final String error;
}
