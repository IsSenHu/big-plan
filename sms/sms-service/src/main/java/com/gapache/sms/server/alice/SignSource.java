package com.gapache.sms.server.alice;

import lombok.Getter;

/**
 * @author HuSen
 * create on 2020/1/13 15:56
 */
@Getter
public enum SignSource {
    _0(0, "企事业单位的全称或简称"),
    _1(1, "工信部备案网站的全称或简称"),
    _2(2, "APP应用的全称或简称"),
    _3(3, "公众号或小程序的全称或简称"),
    _4(4, "电商平台店铺名的全称或简称"),
    _5(5, "商标名的全称或简称");

    SignSource(Integer value, String description) {
        this.value = value;
        this.description = description;
    }

    private Integer value;
    private String description;
}
