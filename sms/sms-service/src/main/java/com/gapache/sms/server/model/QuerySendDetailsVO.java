package com.gapache.sms.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/14 17:19
 */
@Data
public class QuerySendDetailsVO implements Serializable {
    private static final long serialVersionUID = 1884299439468234456L;

    /**
     * 必须
     *
     * 分页查看发送记录，指定发送记录的的当前页码。
     */
    private Long currentPage;

    /**
     * 必须
     *
     * 分页查看发送记录，指定每页显示的短信记录数量。
     * 取值范围为1~50。
     */
    private Long pageSize;

    /**
     * 必须
     *
     * 接收短信的手机号码。
     * 格式：
     * 国内短信：11位手机号码，例如15900000000。
     * 国际/港澳台消息：国际区号+号码，例如85200000000。
     */
    private String phoneNumber;

    /**
     * 必须
     *
     * 短信发送日期，支持查询最近30天的记录。
     * 格式为yyyyMMdd，例如20181225。
     */
    private String sendDate;

    /**
     * 发送回执ID，即发送流水号。调用发送接口SendSms或SendBatchSms发送短信时，返回值中的BizId字段。
     */
    private String bizId;
}
