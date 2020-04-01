package com.gapache.sms.server.alice.response;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/12
 */
@Data
public class BaseSmsResponse implements Serializable {
    private static final long serialVersionUID = -1338784999884151178L;

    @JSONField(name = "Message")
    private String message;

    @JSONField(name = "RequestId")
    private String requestId;

    @JSONField(name = "Code")
    private String code;
}
