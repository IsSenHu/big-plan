package com.gapache.sms.server;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author HuSen
 * create on 2020/1/12
 */
@Data
public class BaseSmsResponse {

    @JSONField(name = "Message")
    private String message;

    @JSONField(name = "RequestId")
    private String requestId;

    @JSONField(name = "Code")
    private String code;
}
