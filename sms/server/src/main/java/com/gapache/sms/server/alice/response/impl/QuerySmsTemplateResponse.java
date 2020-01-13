package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuerySmsTemplateResponse extends BaseSmsResponse {
    private static final long serialVersionUID = 1663163587124749790L;

    @JSONField(name = "TemplateContent")
    private String templateContent;

    @JSONField(name = "TemplateType")
    private Integer templateType;

    @JSONField(name = "TemplateName")
    private String templateName;

    @JSONField(name = "TemplateCode")
    private String templateCode;

    @JSONField(name = "CreateDate", format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JSONField(name = "Reason")
    private String reason;

    @JSONField(name = "TemplateStatus")
    private Integer templateStatus;
}
