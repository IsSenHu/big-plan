package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HuSen
 * create on 2020/1/13 13:35
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AddSmsTemplateResponse extends BaseSmsResponse {
    private static final long serialVersionUID = -3124910721097638867L;

    @JSONField(name = "TemplateCode")
    private String templateCode;
}
