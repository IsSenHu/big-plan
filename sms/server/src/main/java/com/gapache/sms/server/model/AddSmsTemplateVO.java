package com.gapache.sms.server.model;

import com.gapache.sms.server.alice.TemplateType;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/14 14:14
 */
@Data
public class AddSmsTemplateVO implements Serializable {
    private static final long serialVersionUID = 719047051621720121L;

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
}
