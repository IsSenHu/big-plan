package com.gapache.sms.server.alice.request.impl;

import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.request.BaseSmsRequest;
import com.gapache.sms.server.alice.response.impl.SendBatchSmsResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/13 14:58
 */
@Setter
@Getter
@ToString
public class SendBatchSmsRequest extends BaseSmsRequest<SendBatchSmsResponse> {
    private static final long serialVersionUID = -2333975766043526408L;

    /**
     * 必须
     *
     * ["15900000000","13500000000"]
     * 接收短信的手机号码，JSON数组格式。
     * 手机号码格式：
     * 国内短信：11位手机号码，例如15900000000。
     * 国际/港澳台消息：国际区号+号码，例如85200000000。
     * 说明 验证码类型短信，建议使用接口SendSms单独发送。
     */
    private String phoneNumberJson;

    /**
     * 必须
     *
     * ["阿里云","阿里巴巴"]
     * 短信签名名称，JSON数组格式。
     * 请在控制台签名管理页面签名名称一列查看。
     * 说明 必须是已添加、并通过审核的短信签名；且短信签名的个数必须与手机号码的个数相同、内容一一对应。
     */
    private String signNameJson;

    /**
     * 必须
     *
     * 短信模板CODE。请在控制台模板管理页面模板CODE一列查看。
     * 说明 必须是已添加、并通过审核的模板CODE；且发送国际/港澳台消息时，请使用国际/港澳台短信模版。
     */
    private String templateCode;

    /**
     * 上行短信扩展码，JSON数组格式。无特殊需要此字段的用户请忽略此字段。
     */
    private String smsUpExtendCodeJson;

    /**
     * [{"name":"TemplateParamJson"},{"name":"TemplateParamJson"}]
     *
     * 短信模板变量对应的实际值，JSON格式。
     * 说明 如果JSON中需要带换行符，请参照标准的JSON协议处理；
     * 且模板变量值的个数必须与手机号码、签名的个数相同、内容一一对应，表示向指定手机号码中发对应签名的短信，且短信模板中的变量参数替换为对应的值。
     */
    private String templateParamJson;

    public SendBatchSmsRequest(SMSAlice smsAlice, String phoneNumberJson, String signNameJson, String templateCode, String smsUpExtendCodeJson, String templateParamJson) {
        super(smsAlice);
        this.phoneNumberJson = phoneNumberJson;
        this.signNameJson = signNameJson;
        this.templateCode = templateCode;
        this.smsUpExtendCodeJson = smsUpExtendCodeJson;
        this.templateParamJson = templateParamJson;
    }

    @Override
    protected Map<String, String> buildQueryParameters() {
        Map<String, String> queryParameters = new HashMap<>(5);
        queryParameters.put("PhoneNumberJson", this.phoneNumberJson);
        queryParameters.put("SignNameJson", this.signNameJson);
        queryParameters.put("TemplateCode", this.templateCode);
        if (StringUtils.isNotBlank(this.smsUpExtendCodeJson)) {
            queryParameters.put("SmsUpExtendCodeJson", this.smsUpExtendCodeJson);
        }
        if (StringUtils.isNotBlank(this.templateParamJson)) {
            queryParameters.put("TemplateParamJson", this.templateParamJson);
        }
        return queryParameters;
    }

    @Override
    protected SysAction getSysAction() {
        return SysAction.SEND_BATCH_SMS;
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    protected Class<SendBatchSmsResponse> responseClazz() {
        return SendBatchSmsResponse.class;
    }

    @Override
    protected boolean checkQueryParameters() {
        return StringUtils.isNotBlank(this.phoneNumberJson) && StringUtils.isNotBlank(this.signNameJson) && StringUtils.isNotBlank(this.templateCode);
    }
}
