package com.gapache.sms.server;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/1/12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class QuerySendDetailsResponse extends BaseSmsResponse implements Serializable {
    private static final long serialVersionUID = -2031330648492981964L;

    @JSONField(name = "TotalCount")
    private Long totalCount;

    @JSONField(name = "SmsSendDetailDTOs")
    private List<SmsSendDetail> smsSendDetails;
}
