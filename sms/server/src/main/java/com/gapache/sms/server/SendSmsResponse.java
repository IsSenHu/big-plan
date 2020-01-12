package com.gapache.sms.server;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SendSmsResponse extends BaseSmsResponse implements Serializable {
    private static final long serialVersionUID = 186475350419659374L;

    @JSONField(name = "bizId")
    private String BizId;
}
