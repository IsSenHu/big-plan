package com.gapache.sms.server.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/14 15:45
 */
@Data
public class QuerySmsTemplateVO implements Serializable {
    private static final long serialVersionUID = 1562590079029791918L;

    private String templateContent;

    private Integer templateType;

    private String templateName;

    private String templateCode;

    private LocalDateTime createDate;

    private String reason;

    /**
     * 模板审核状态。其中：
     * 0：审核中。
     * 1：审核通过。
     * 2：审核失败，请在返回参数Reason中查看审核失败原因。
     */
    private Integer templateStatus;
}
