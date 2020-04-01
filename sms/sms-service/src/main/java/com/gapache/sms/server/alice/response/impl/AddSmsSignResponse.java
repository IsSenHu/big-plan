package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HuSen
 * create on 2020/1/13 13:37
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddSmsSignResponse extends BaseSmsResponse {
    private static final long serialVersionUID = -3439298697284955922L;

    @JSONField(name = "SignName")
    private String signName;
}
