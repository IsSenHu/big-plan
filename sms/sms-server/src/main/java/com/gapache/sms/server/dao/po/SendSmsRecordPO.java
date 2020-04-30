package com.gapache.sms.server.dao.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * create on 2020/1/14 14:59
 */
@Getter
@Setter
@Entity
@Table(name = "tb_send_sms_record")
public class SendSmsRecordPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 必须
     *
     * 接收短信的手机号码。
     * 国内短信：11位手机号码，例如15951955195。
     * 国际/港澳台消息：国际区号+号码，例如85200000000。
     * 支持对多个手机号码发送短信，手机号码之间以英文逗号（,）分隔。上限为1000个手机号码。批量调用相对于单条调用及时性稍有延迟。
     * 说明 验证码类型短信，建议使用单独发送的方式。
     */
    @Column(nullable = false, length = 11000)
    private String phoneNumbers;

    /**
     * 必须
     *
     * 短信签名名称。请在控制台签名管理页面签名名称一列查看。
     * 说明 必须是已添加、并通过审核的短信签名。
     */
    @Column(nullable = false, length = 20)
    private String signName;

    /**
     * 必须
     *
     * 短信模板ID。请在控制台模板管理页面模板CODE一列查看。
     * 说明 必须是已添加、并通过审核的短信签名；且发送国际/港澳台消息时，请使用国际/港澳台短信模版。
     */
    @Column(nullable = false, length = 20)
    private String templateCode;

    /**
     * 上行短信扩展码，无特殊需要此字段的用户请忽略此字段。
     */
    @Column(length = 20)
    private String smsUpExtendCode;

    /**
     * 短信模板变量对应的实际值，JSON格式。
     * 说明 如果JSON中需要带换行符，请参照标准的JSON协议处理。
     */
    @Column
    private String templateParam;

    /**
     * 外部流水扩展字段。
     */
    @Column(length = 50)
    private String outId;

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
