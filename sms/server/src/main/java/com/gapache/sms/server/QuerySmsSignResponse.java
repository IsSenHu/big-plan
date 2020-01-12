package com.gapache.sms.server;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class QuerySmsSignResponse extends BaseSmsResponse implements Serializable {
    private static final long serialVersionUID = -6253740883509005265L;

    @JSONField(name = "CreateDate", format = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @JSONField(name = "Reason")
    private String reason;

    @JSONField(name = "SignName")
    private String signName;

    @JSONField(name = "SignStatus")
    private Integer signStatus;
}
