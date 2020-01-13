package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HuSen
 * create on 2020/1/13 13:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendBatchSmsResponse extends BaseSmsResponse {
    private static final long serialVersionUID = -1517845539183209324L;

    @JSONField(name = "BizId")
    private String bizId;
}
