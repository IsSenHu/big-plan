package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HuSen
 * create on 2020/1/13 13:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ModifySmsSignResponse extends BaseSmsResponse {
    private static final long serialVersionUID = 838939180984989676L;

    @JSONField(name = "SignName")
    private String signName;
}
