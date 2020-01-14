package com.gapache.sms.server.alice.request.impl;

import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.request.BaseSmsRequest;
import com.gapache.sms.server.alice.response.impl.SendSmsResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/13 14:07
 */
@Getter
@Setter
@ToString
public class SendSmsRequest extends BaseSmsRequest<SendSmsResponse> {
    private static final long serialVersionUID = -3644648289336163876L;

    /**
     * 必须
     *
     * 接收短信的手机号码。
     * 国内短信：11位手机号码，例如15951955195。
     * 国际/港澳台消息：国际区号+号码，例如85200000000。
     * 支持对多个手机号码发送短信，手机号码之间以英文逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟。
     * 说明 验证码类型短信，建议使用单独发送的方式。
     */
    private String phoneNumbers;

    /**
     * 必须
     *
     * 短信签名名称。请在控制台签名管理页面签名名称一列查看。
     * 说明 必须是已添加、并通过审核的短信签名。
     */
    private String signName;

    /**
     * 必须
     *
     * 短信模板ID。请在控制台模板管理页面模板CODE一列查看。
     * 说明 必须是已添加、并通过审核的短信签名；且发送国际/港澳台消息时，请使用国际/港澳台短信模版。
     */
    private String templateCode;

    /**
     * 上行短信扩展码，无特殊需要此字段的用户请忽略此字段。
     */
    private String smsUpExtendCode;

    /**
     * 短信模板变量对应的实际值，JSON格式。
     * 说明 如果JSON中需要带换行符，请参照标准的JSON协议处理。
     */
    private String templateParam;

    /**
     * 外部流水扩展字段。
     */
    private String outId;

    public SendSmsRequest(SMSAlice smsAlice, String phoneNumbers, String signName, String templateCode, String smsUpExtendCode, String templateParam, String outId) {
        super(smsAlice);
        this.phoneNumbers = phoneNumbers;
        this.signName = signName;
        this.templateCode = templateCode;
        this.smsUpExtendCode = smsUpExtendCode;
        this.templateParam = templateParam;
        this.outId = outId;
    }

    @Override
    protected Map<String, String> buildQueryParameters() {
        Map<String, String> queryParameters = new HashMap<>(6);
        queryParameters.put("PhoneNumbers", this.phoneNumbers);
        queryParameters.put("SignName", this.signName);
        queryParameters.put("TemplateCode", this.templateCode);
        if (StringUtils.isNotBlank(this.outId)) {
            queryParameters.put("OutId", this.outId);
        }
        if (StringUtils.isNotBlank(this.smsUpExtendCode)) {
            queryParameters.put("SmsUpExtendCode", this.smsUpExtendCode);
        }
        if (StringUtils.isNotBlank(this.templateParam)) {
            queryParameters.put("TemplateParam", this.templateParam);
        }
        return queryParameters;
    }

    @Override
    protected SysAction getSysAction() {
        return SysAction.SEND_SMS;
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    protected Class<SendSmsResponse> responseClazz() {
        return SendSmsResponse.class;
    }

    @Override
    protected boolean checkQueryParameters() {
        return StringUtils.isNotBlank(this.phoneNumbers) && StringUtils.isNotBlank(this.signName) && StringUtils.isNotBlank(this.templateCode);
    }
}
