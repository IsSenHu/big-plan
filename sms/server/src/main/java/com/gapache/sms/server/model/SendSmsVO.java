package com.gapache.sms.server.model;

import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * create on 2020/1/14 11:49
 */
@Data
public class SendSmsVO implements Serializable {
    private static final long serialVersionUID = 9165750415207005766L;

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

    /**
     * 验证码的时候使用 有效时间
     */
    private Long effectiveTime;

    /**
     * 给每个用户发送短信的间隔时间
     */
    private Long intervals;

    /**
     * 有效时间 时间单位
     */
    private TimeUnit timeUnit;
}
