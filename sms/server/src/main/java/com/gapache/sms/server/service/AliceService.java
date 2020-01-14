package com.gapache.sms.server.service;

import com.gapache.commons.model.JsonResult;
import com.gapache.sms.server.model.*;

/**
 * @author HuSen
 * create on 2020/1/14 11:47
 */
public interface AliceService {

    JsonResult<String> sendSms(SendSmsVO vo);

    JsonResult<String> addSmsTemplate(AddSmsTemplateVO vo);

    JsonResult<QuerySmsTemplateVO> querySmsTemplate(QuerySmsTemplateVO vo);

    JsonResult<String> addSmsSign(AddSmsSignVO vo);

    JsonResult<String> deleteSmsSign(DeleteSmsSignVO vo);

    JsonResult<String> deleteSmsTemplate(DeleteSmsTemplateVO vo);

    JsonResult<String> modifySmsSign(ModifySmsSignVO vo);

    JsonResult<String> modifySmsTemplate(ModifySmsTemplateVO vo);

    JsonResult<QuerySmsSignVO> querySmsSign(QuerySmsSignVO vo);

    JsonResult<String> sendBatchSms(SendBatchSmsVO vo);

    JsonResult<SendDetailsVO> querySendDetails(QuerySendDetailsVO vo);
}
