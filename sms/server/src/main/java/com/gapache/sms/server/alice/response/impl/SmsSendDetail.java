package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/12
 */
@Data
public class SmsSendDetail implements Serializable {
    private static final long serialVersionUID = 1521869104513302803L;

    @JSONField(name = "SendDate", format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sendDate;

    @JSONField(name = "SendStatus")
    private Integer sendStatus;

    @JSONField(name = "ReceiveDate", format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveDate;

    @JSONField(name = "ErrCode")
    private String errCode;

    @JSONField(name = "TemplateCode")
    private String templateCode;

    @JSONField(name = "Content")
    private String content;

    @JSONField(name = "PhoneNum")
    private String phoneNum;
}
