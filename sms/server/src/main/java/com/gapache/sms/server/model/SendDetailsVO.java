package com.gapache.sms.server.model;

import com.gapache.sms.server.alice.response.impl.SmsSendDetail;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/1/14 17:19
 */
@Data
public class SendDetailsVO implements Serializable {
    private static final long serialVersionUID = 632671423534335710L;

    private Long totalCount;

    private List<SmsSendDetail> smsSendDetails;
}
