package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuerySmsSignResponse extends BaseSmsResponse {
    private static final long serialVersionUID = -6253740883509005265L;

    @JSONField(name = "CreateDate", format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JSONField(name = "Reason")
    private String reason;

    @JSONField(name = "SignName")
    private String signName;

    /**
     * 签名审核状态。其中：
     * 0：审核中。
     * 1：审核通过。
     * 2：审核失败，请在返回参数Reason中查看审核失败原因。
     */
    @JSONField(name = "SignStatus")
    private Integer signStatus;
}
