package com.gapache.sms.server.dao.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * create on 2020/1/14 17:01
 */
@Getter
@Setter
@Entity
@Table(name = "tb_send_batch_sms_record")
public class SendBatchSmsRecordPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @Column(nullable = false, length = 1000)
    private String phoneNumberJson;

    /**
     * 必须
     *
     * ["阿里云","阿里巴巴"]
     * 短信签名名称，JSON数组格式。
     * 请在控制台签名管理页面签名名称一列查看。
     * 说明 必须是已添加、并通过审核的短信签名；且短信签名的个数必须与手机号码的个数相同、内容一一对应。
     */
    @Column(nullable = false, length = 1000)
    private String signNameJson;

    /**
     * 必须
     *
     * 短信模板CODE。请在控制台模板管理页面模板CODE一列查看。
     * 说明 必须是已添加、并通过审核的模板CODE；且发送国际/港澳台消息时，请使用国际/港澳台短信模版。
     */
    @Column(nullable = false, length = 50)
    private String templateCode;

    /**
     * 上行短信扩展码，JSON数组格式。无特殊需要此字段的用户请忽略此字段。
     */
    @Column(length = 1000)
    private String smsUpExtendCodeJson;

    /**
     * [{"name":"TemplateParamJson"},{"name":"TemplateParamJson"}]
     *
     * 短信模板变量对应的实际值，JSON格式。
     * 说明 如果JSON中需要带换行符，请参照标准的JSON协议处理；
     * 且模板变量值的个数必须与手机号码、签名的个数相同、内容一一对应，表示向指定手机号码中发对应签名的短信，且短信模板中的变量参数替换为对应的值。
     */
    @Column(length = 1000)
    private String templateParamJson;

    /**
     * 发送回执ID，可根据该ID在接口QuerySendDetails中查询具体的发送状态。
     */
    @Column(length = 50)
    private String bizId;

    /**
     * 状态码的描述。
     */
    @Column(length = 50)
    private String message;

    /**
     * 请求ID。
     */
    @Column(length = 50, unique = true)
    private String requestId;

    /**
     * 请求状态码。
     * 返回OK代表请求成功。
     * 其他错误码详见错误码列表。
     */
    @Column(nullable = false, length = 50)
    private String code;

    /**
     * 请求发送时间
     */
    @Column(nullable = false)
    private LocalDateTime sendTime;

    /**
     * 验证码的时候使用 有效时间
     */
    @Column
    private Long effectiveTime;

    /**
     * 给每个用户发送短信的间隔时间
     */
    @Column(nullable = false)
    private Long intervals;

    /**
     * 有效时间 时间单位
     */
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TimeUnit timeUnit;
}
