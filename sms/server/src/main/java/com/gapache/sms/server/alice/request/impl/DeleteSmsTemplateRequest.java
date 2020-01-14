package com.gapache.sms.server.alice.request.impl;

import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.request.BaseSmsRequest;
import com.gapache.sms.server.alice.response.impl.DeleteSmsTemplateResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/13 15:38
 */
@Setter
@Getter
@ToString
public class DeleteSmsTemplateRequest extends BaseSmsRequest<DeleteSmsTemplateResponse> {
    private static final long serialVersionUID = 8068224251175829995L;

    /**
     * 必须
     */
    private String templateCode;

    public DeleteSmsTemplateRequest(SMSAlice smsAlice, String templateCode) {
        super(smsAlice);
        this.templateCode = templateCode;
    }

    @Override
    protected Map<String, String> buildQueryParameters() {
        Map<String, String> queryParameters = new HashMap<>(1);
        queryParameters.put("TemplateCode", this.templateCode);
        return queryParameters;
    }

    @Override
    protected SysAction getSysAction() {
        return SysAction.DELETE_SMS_TEMPLATE;
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    protected Class<DeleteSmsTemplateResponse> responseClazz() {
        return DeleteSmsTemplateResponse.class;
    }

    @Override
    protected boolean checkQueryParameters() {
        return StringUtils.isNotBlank(this.templateCode);
    }
}
