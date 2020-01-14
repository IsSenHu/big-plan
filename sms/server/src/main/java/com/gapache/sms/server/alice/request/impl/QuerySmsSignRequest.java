package com.gapache.sms.server.alice.request.impl;

import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.request.BaseSmsRequest;
import com.gapache.sms.server.alice.response.impl.QuerySmsSignResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/13 15:22
 */
@Setter
@Getter
@ToString
public class QuerySmsSignRequest extends BaseSmsRequest<QuerySmsSignResponse> {
    private static final long serialVersionUID = 6085983838152553410L;

    /**
     * 必须
     */
    private String signName;

    public QuerySmsSignRequest(SMSAlice smsAlice, String signName) {
        super(smsAlice);
        this.signName = signName;
    }

    @Override
    protected Map<String, String> buildQueryParameters() {
        Map<String, String> queryParameters = new HashMap<>(1);
        queryParameters.put("SignName", this.signName);
        return queryParameters;
    }

    @Override
    protected SysAction getSysAction() {
        return SysAction.QUERY_SMS_SIGN;
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    protected Class<QuerySmsSignResponse> responseClazz() {
        return QuerySmsSignResponse.class;
    }

    @Override
    protected boolean checkQueryParameters() {
        return StringUtils.isNotBlank(this.signName);
    }
}
