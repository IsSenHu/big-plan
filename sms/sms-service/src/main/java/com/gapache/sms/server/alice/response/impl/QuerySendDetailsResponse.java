package com.gapache.sms.server.alice.response.impl;

import com.alibaba.fastjson.annotation.JSONField;
import com.gapache.sms.server.alice.response.BaseSmsResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HuSen
 * create on 2020/1/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuerySendDetailsResponse extends BaseSmsResponse {
    private static final long serialVersionUID = -2031330648492981964L;

    @JSONField(name = "TotalCount")
    private Long totalCount;

    @JSONField(name = "SmsSendDetailDTOs")
    private SmsSendDetails smsSendDetails;
}
