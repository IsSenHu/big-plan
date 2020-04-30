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

    /**
     * 短信发送状态，包括：
     * 1：等待回执。
     * 2：发送失败。
     * 3：发送成功。
     */
    @JSONField(name = "SendStatus")
    private Integer sendStatus;

    @JSONField(name = "ReceiveDate", format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveDate;

    /**
     * 运营商短信状态码。
     * 短信发送成功：DELIVERED。
     * 短信发送失败：失败错误码请参考错误码文档。
     * <a href="https://help.aliyun.com/document_detail/101347.html?spm=a2c4g.11186623.2.14.1722bc45yhvrM5"></a>
     */
    @JSONField(name = "ErrCode")
    private String errCode;

    @JSONField(name = "TemplateCode")
    private String templateCode;

    @JSONField(name = "Content")
    private String content;

    @JSONField(name = "PhoneNum")
    private String phoneNum;
}
