package com.gapache.sms.server.alice.request.impl;

import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.TemplateType;
import com.gapache.sms.server.alice.request.BaseSmsRequest;
import com.gapache.sms.server.alice.response.impl.AddSmsTemplateResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @author HuSen
 * create on 2020/1/13 14:45
 */
@Getter
@Setter
@ToString
public class AddSmsTemplateRequest extends BaseSmsRequest<AddSmsTemplateResponse> {
    private static final long serialVersionUID = -1533414292041541901L;

    /**
     * 必须
     * <p>
     * 短信类型。其中：
     * 0：验证码。
     * 1：短信通知。
     * 2：推广短信。
     * 3：国际/港澳台消息。
     */
    private TemplateType templateType;

    /**
     * 必须
     * <p>
     * 模板名称，长度为1~30个字符。
     */
    private String templateName;

    /**
     * 必须
     * <p>
     * 模板内容，长度为1~500个字符。
     * 模板内容需要符合文本短信模板规范或国际/港澳台短信模板规范。
     */
    private String templateContent;

    /**
     * 必须
     * <p>
     * 短信模板申请说明。请在申请说明中描述您的业务使用场景，长度为1~100个字符。
     */
    private String remark;

    public AddSmsTemplateRequest(SMSAlice smsAlice, TemplateType templateType, String templateName, String templateContent, String remark) {
        super(smsAlice);
        this.templateType = templateType;
        this.templateName = templateName;
        this.templateContent = templateContent;
        this.remark = remark;
    }

    @Override
    protected Map<String, String> buildQueryParameters() {
        return buildAddOrModifyTemplateQueryParameters(
                this.templateType.getValue().toString(),
                this.templateName,
                this.templateContent,
                this.remark
        );
    }

    @Override
    protected SysAction getSysAction() {
        return SysAction.ADD_SMS_TEMPLATE;
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    protected Class<AddSmsTemplateResponse> responseClazz() {
        return AddSmsTemplateResponse.class;
    }

    @Override
    protected boolean checkQueryParameters() {
        return Objects.nonNull(this.templateType) && StringUtils.isNotBlank(this.templateName) && StringUtils.isNotBlank(this.templateContent) && StringUtils.isNotBlank(this.remark);
    }
}
