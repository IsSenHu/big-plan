package com.gapache.sms.server.alice.request.impl;

import com.aliyuncs.http.MethodType;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.SysAction;
import com.gapache.sms.server.alice.request.BaseSmsRequest;
import com.gapache.sms.server.alice.response.impl.QuerySendDetailsResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author HuSen
 * create on 2020/1/13 15:09
 */
@Setter
@Getter
@ToString
public class QuerySendDetailsRequest extends BaseSmsRequest<QuerySendDetailsResponse> {
    private static final long serialVersionUID = -3968253348526136260L;

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

    public QuerySendDetailsRequest(SMSAlice smsAlice, Long currentPage, Long pageSize, String phoneNumber, String sendDate, String bizId) {
        super(smsAlice);
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.phoneNumber = phoneNumber;
        this.sendDate = sendDate;
        this.bizId = bizId;
    }

    @Override
    protected Map<String, String> buildQueryParameters() {
        Map<String, String> queryParameters = new HashMap<>(5);
        queryParameters.put("CurrentPage", this.currentPage.toString());
        queryParameters.put("PageSize", this.pageSize.toString());
        queryParameters.put("PhoneNumber", this.phoneNumber);
        queryParameters.put("SendDate", this.sendDate);
        if (StringUtils.isNotBlank(this.bizId)) {
            queryParameters.put("BizId", this.bizId);
        }
        return queryParameters;
    }

    @Override
    protected SysAction getSysAction() {
        return SysAction.QUERY_SEND_DETAILS;
    }

    @Override
    protected MethodType getMethodType() {
        return MethodType.POST;
    }

    @Override
    protected Class<QuerySendDetailsResponse> responseClazz() {
        return QuerySendDetailsResponse.class;
    }

    @Override
    protected boolean checkQueryParameters() {
        return Objects.nonNull(this.currentPage) && Objects.nonNull(this.pageSize) && this.pageSize <= 50 && this.pageSize >= 1
                && StringUtils.isNotBlank(this.phoneNumber) && StringUtils.isNotBlank(this.sendDate);
    }
}
