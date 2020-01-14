package com.gapache.sms.server.alice.request.impl;

import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.request.BaseSmsRequest;
import com.gapache.sms.server.alice.response.impl.DeleteSmsSignResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/13 15:36
 */
@Setter
@Getter
@ToString
public class DeleteSmsSignRequest extends BaseSmsRequest<DeleteSmsSignResponse> {
    private static final long serialVersionUID = 2288148286156147363L;

    /**
     * 必须
     */
    private String signName;

    public DeleteSmsSignRequest(SMSAlice smsAlice, String signName) {
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
        return SysAction.DELETE_SMS_SIGN;
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    protected Class<DeleteSmsSignResponse> responseClazz() {
        return DeleteSmsSignResponse.class;
    }

    @Override
    protected boolean checkQueryParameters() {
        return StringUtils.isNotBlank(this.signName);
    }
}
