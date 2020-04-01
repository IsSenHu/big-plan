package com.gapache.sms.server.alice;

import lombok.Getter;

/**
 * @author HuSen
 * create on 2020/1/11
 */
@Getter
public enum SysAction {
    SEND_SMS("SendSms"),
    SEND_BATCH_SMS("SendBatchSms"),
    ADD_SMS_SIGN("AddSmsSign"),
    ADD_SMS_TEMPLATE("AddSmsTemplate"),
    QUERY_SEND_DETAILS("QuerySendDetails"),
    QUERY_SMS_SIGN("QuerySmsSign"),
    QUERY_SMS_TEMPLATE("QuerySmsTemplate"),
    MODIFY_SMS_SIGN("ModifySmsSign"),
    MODIFY_SMS_TEMPLATE("ModifySmsTemplate"),
    DELETE_SMS_SIGN("DeleteSmsSign"),
    DELETE_SMS_TEMPLATE("DeleteSmsTemplate");
    private String value;

    SysAction(String value) {
        this.value = value;
    }
}
