package com.gapache.sms.server.alice;

import lombok.Getter;

/**
 * @author HuSen
 * create on 2020/1/13 16:02
 */
@Getter
public enum TemplateType {
    _0(0, "验证码"),
    _1(1, "短信通知"),
    _2(2, "推广短信"),
    _3(3, "国际/港澳台消息");
    private Integer value;
    private String description;

    TemplateType(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    public static TemplateType fromValue(Integer value) {
        return TemplateType.valueOf("_" + value);
    }
}
