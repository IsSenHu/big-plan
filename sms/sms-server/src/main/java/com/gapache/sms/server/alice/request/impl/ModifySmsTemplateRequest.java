package com.gapache.sms.server.alice.request.impl;

import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.TemplateType;
import com.gapache.sms.server.alice.request.BaseSmsRequest;
import com.gapache.sms.server.alice.response.impl.ModifySmsTemplateResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author HuSen
 * create on 2020/1/13 15:33
 */
@Setter
@Getter
@ToString
public class ModifySmsTemplateRequest extends BaseSmsRequest<ModifySmsTemplateResponse> {
    private static final long serialVersionUID = 4314154251675921106L;

    /**
     * 必须
     *
     * 短信类型。其中：
     * 0：验证码。
     * 1：短信通知。
     * 2：推广短信。
     * 3：国际/港澳台消息。
     */
    private TemplateType templateType;

    /**
     * 必须
     *
     * 模板名称，长度为1~30个字符。
     */
    private String templateName;

    /**
     * 必须
     *
     * 模板内容，长度为1~500个字符。
     * 模板内容需要符合文本短信模板规范或国际/港澳台短信模板规范。
     */
    private String templateContent;

    /**
     * 必须
     *
     * 短信模板申请说明。请在申请说明中描述您的业务使用场景，长度为1~100个字符。
     */
    private String remark;

    /**
     * 必须
     *
     * 短信模板CODE。您可以在控制台模板管理页面或API接口AddSmsTemplate的返回参数中获取短信模板CODE。
     */
    private String templateCode;

    public ModifySmsTemplateRequest(SMSAlice smsAlice, TemplateType templateType, String templateName, String templateContent, String remark, String templateCode) {
        super(smsAlice);
        this.templateType = templateType;
        this.templateName = templateName;
        this.templateContent = templateContent;
        this.remark = remark;
        this.templateCode = templateCode;
    }

    @Override
    protected Map<String, String> buildQueryParameters() {
        Map<String, String> queryParameters = buildAddOrModifyTemplateQueryParameters(
                this.templateType.getValue().toString(),
                this.templateName,
                this.templateContent,
                this.remark
        );
        queryParameters.put("TemplateCode", this.templateCode);
        return queryParameters;
    }

    @Override
    protected SysAction getSysAction() {
        return SysAction.MODIFY_SMS_TEMPLATE;
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    protected Class<ModifySmsTemplateResponse> responseClazz() {
        return ModifySmsTemplateResponse.class;
    }

    @Override
    protected boolean checkQueryParameters() {
        return Objects.nonNull(this.templateType) && StringUtils.isNotBlank(this.templateName)
                && StringUtils.isNotBlank(this.templateContent) && StringUtils.isNotBlank(this.remark)
                && StringUtils.isNotBlank(this.templateCode);
    }
}
