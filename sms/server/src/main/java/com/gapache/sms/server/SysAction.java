package com.gapache.sms.server;

import lombok.Getter;

/**
 * @author HuSen
 * create on 2020/1/11
 */
@Getter
public enum SysAction {
    ;
    private String value;

    SysAction(String value) {
        this.value = value;
    }
}
