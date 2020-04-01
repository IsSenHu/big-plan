package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HuSen
 * create on 2020/1/13 13:43
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DeleteSmsTemplateResponse extends BaseSmsResponse {
    private static final long serialVersionUID = 856967948251465491L;

    @JSONField(name = "TemplateCode")
    private String templateCode;
}
