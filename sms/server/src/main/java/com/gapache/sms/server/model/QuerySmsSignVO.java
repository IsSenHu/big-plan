package com.gapache.sms.server.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/14 16:49
 */
@Data
public class QuerySmsSignVO implements Serializable {
    private static final long serialVersionUID = 6760029927171455648L;

    private LocalDateTime createDate;

    private String reason;

    private String signName;

    /**
     * 签名审核状态。其中：
     * 0：审核中。
     * 1：审核通过。
     * 2：审核失败，请在返回参数Reason中查看审核失败原因。
     */
    private Integer signStatus;
}
