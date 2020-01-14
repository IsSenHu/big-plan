package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/1/14 17:38
 */
@Data
public class SmsSendDetails {

    @JSONField(name = "SmsSendDetailDTO")
    private List<SmsSendDetail> details;
}
