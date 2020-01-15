package com.gapache.sms.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.request.impl.*;
import com.gapache.sms.server.alice.response.impl.*;
import com.gapache.sms.server.dao.po.SendBatchSmsRecordPO;
import com.gapache.sms.server.dao.po.SendSmsRecordPO;
import com.gapache.sms.server.dao.repository.SendBatchSmsRecordRepository;
import com.gapache.sms.server.dao.repository.SendSmsRecordRepository;
import com.gapache.sms.server.model.*;
import com.gapache.sms.server.service.AliceService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/14 11:47
 */
@Service
public class AliceServiceImpl implements AliceService {

    private final SMSAlice smsAlice;
    private final SendSmsRecordRepository sendSmsRecordRepository;
    private final SendBatchSmsRecordRepository sendBatchSmsRecordRepository;

    public AliceServiceImpl(SMSAlice smsAlice, SendSmsRecordRepository sendSmsRecordRepository, SendBatchSmsRecordRepository sendBatchSmsRecordRepository) {
        this.smsAlice = smsAlice;
        this.sendSmsRecordRepository = sendSmsRecordRepository;
        this.sendBatchSmsRecordRepository = sendBatchSmsRecordRepository;
    }

    @Override
    public JsonResult<String> sendSms(SendSmsVO vo) {
        SendSmsRequest request = new SendSmsRequest(
                smsAlice,
                vo.getPhoneNumbers(),
                vo.getSignName(),
                vo.getTemplateCode(),
                vo.getSmsUpExtendCode(),
                JSON.toJSONString(vo.getTemplateParam()),
                vo.getOutId());
        LocalDateTime now = LocalDateTime.now();
        SendSmsResponse response = request.getResponse();

        saveSendSmsRecord(vo, response, now);

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);
        return JsonResult.of(response.getBizId());
    }

    @Override
    public JsonResult<String> sendBatchSms(SendBatchSmsVO vo) {
        SendBatchSmsRequest request = new SendBatchSmsRequest(
                smsAlice,
                vo.getPhoneNumberJson(),
                vo.getSignNameJson(),
                vo.getTemplateCode(),
                vo.getSmsUpExtendCodeJson(),
                vo.getTemplateParamJson()
        );
        LocalDateTime now = LocalDateTime.now();
        SendBatchSmsResponse response = request.getResponse();

        saveSendBatchSmsRecord(vo, response, now);

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        return JsonResult.of(response.getBizId());
    }

    @Override
    public JsonResult<String> addSmsTemplate(AddSmsTemplateVO vo) {
        AddSmsTemplateRequest request = new AddSmsTemplateRequest(
                smsAlice,
                vo.getTemplateType(),
                vo.getTemplateName(),
                vo.getTemplateContent(),
                vo.getRemark()
        );
        AddSmsTemplateResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        return JsonResult.of(response.getTemplateCode());
    }

    @Override
    public JsonResult<QuerySmsTemplateVO> querySmsTemplate(QuerySmsTemplateVO vo) {
        QuerySmsTemplateRequest request = new QuerySmsTemplateRequest(smsAlice, vo.getTemplateCode());
        QuerySmsTemplateResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        vo.setCreateDate(response.getCreateDate());
        vo.setReason(response.getReason());
        vo.setTemplateCode(response.getTemplateCode());
        vo.setTemplateContent(response.getTemplateContent());
        vo.setTemplateName(response.getTemplateName());
        vo.setTemplateStatus(response.getTemplateStatus());
        vo.setTemplateType(response.getTemplateType());
        return JsonResult.of(vo);
    }

    @Override
    public JsonResult<String> addSmsSign(AddSmsSignVO vo) {
        AddSmsSignRequest request = new AddSmsSignRequest(
                smsAlice,
                vo.getSignName(),
                vo.getSignSource(),
                vo.getRemark(),
                vo.getFileSuffix(),
                vo.getFileContents()
        );
        AddSmsSignResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        return JsonResult.of(response.getSignName());
    }

    @Override
    public JsonResult<String> deleteSmsSign(DeleteSmsSignVO vo) {
        DeleteSmsSignRequest request = new DeleteSmsSignRequest(smsAlice, vo.getSignName());
        DeleteSmsSignResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        return JsonResult.of(response.getSignName());
    }

    @Override
    public JsonResult<String> deleteSmsTemplate(DeleteSmsTemplateVO vo) {
        DeleteSmsTemplateRequest request = new DeleteSmsTemplateRequest(smsAlice, vo.getTemplateCode());
        DeleteSmsTemplateResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        return JsonResult.of(response.getTemplateCode());
    }

    @Override
    public JsonResult<String> modifySmsSign(ModifySmsSignVO vo) {
        ModifySmsSignRequest request = new ModifySmsSignRequest(
                smsAlice,
                vo.getSignName(),
                vo.getSignSource(),
                vo.getRemark(),
                vo.getFileSuffix(),
                vo.getFileContents()
        );
        ModifySmsSignResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        return JsonResult.of(response.getSignName());
    }

    @Override
    public JsonResult<String> modifySmsTemplate(ModifySmsTemplateVO vo) {
        ModifySmsTemplateRequest request = new ModifySmsTemplateRequest(
                smsAlice,
                vo.getTemplateType(),
                vo.getTemplateName(),
                vo.getTemplateContent(),
                vo.getRemark(),
                vo.getTemplateCode()
        );
        ModifySmsTemplateResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        return JsonResult.of(response.getTemplateCode());
    }

    @Override
    public JsonResult<QuerySmsSignVO> querySmsSign(QuerySmsSignVO vo) {
        QuerySmsSignRequest request = new QuerySmsSignRequest(
                smsAlice,
                vo.getSignName()
        );
        QuerySmsSignResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        vo.setCreateDate(response.getCreateDate());
        vo.setReason(response.getReason());
        vo.setSignStatus(response.getSignStatus());
        return JsonResult.of(vo);
    }

    @Override
    public JsonResult<SendDetailsVO> querySendDetails(QuerySendDetailsVO vo) {
        QuerySendDetailsRequest request = new QuerySendDetailsRequest(
                smsAlice,
                vo.getCurrentPage(),
                vo.getPageSize(),
                vo.getPhoneNumber(),
                vo.getSendDate(),
                vo.getBizId()
        );
        QuerySendDetailsResponse response = request.getResponse();

        ThrowUtils.throwIfTrue(response == null, AliceError.REQUEST_EXCEPTION);
        ThrowUtils.throwIfTrue(smsAlice.fail(response.getCode()), AliceError.REQUEST_FAIL);

        SendDetailsVO details = new SendDetailsVO();
        details.setTotalCount(response.getTotalCount());
        details.setSmsSendDetails(response.getSmsSendDetails().getDetails());
        return JsonResult.of(details);
    }

    private void saveSendSmsRecord(SendSmsVO vo, SendSmsResponse response, LocalDateTime sendTime) {
        SendSmsRecordPO record = new SendSmsRecordPO();
        if (response != null) {
            record.setBizId(response.getBizId());
            record.setCode(response.getCode());
            record.setMessage(response.getMessage());
            record.setRequestId(response.getRequestId());
        } else {
            record.setCode(AliceError.REQUEST_FAIL.getCode().toString());
            record.setMessage(AliceError.REQUEST_FAIL.getError());
        }
        record.setOutId(vo.getOutId());
        record.setPhoneNumbers(vo.getPhoneNumbers());
        record.setSendTime(sendTime);
        record.setSignName(vo.getSignName());
        record.setSmsUpExtendCode(vo.getSmsUpExtendCode());
        record.setTemplateCode(vo.getTemplateCode());
        record.setTemplateParam(JSON.toJSONString(vo.getTemplateParam()));
        record.setEffectiveTime(vo.getEffectiveTime());
        record.setTimeUnit(vo.getTimeUnit());
        record.setIntervals(vo.getIntervals());
        sendSmsRecordRepository.save(record);
    }

    private void saveSendBatchSmsRecord(SendBatchSmsVO vo, SendBatchSmsResponse response, LocalDateTime sendTime) {
        SendBatchSmsRecordPO record = new SendBatchSmsRecordPO();
        if (response != null) {
            record.setBizId(response.getBizId());
            record.setCode(response.getCode());
            record.setMessage(response.getMessage());
            record.setRequestId(response.getRequestId());
        } else {
            record.setCode(AliceError.REQUEST_FAIL.getCode().toString());
            record.setMessage(AliceError.REQUEST_FAIL.getError());
        }
        record.setPhoneNumberJson(vo.getPhoneNumberJson());
        record.setSignNameJson(vo.getSignNameJson());
        record.setSendTime(sendTime);
        record.setSmsUpExtendCodeJson(vo.getSmsUpExtendCodeJson());
        record.setTemplateCode(vo.getTemplateCode());
        record.setTemplateParamJson(vo.getTemplateParamJson());
        record.setEffectiveTime(vo.getEffectiveTime());
        record.setTimeUnit(vo.getTimeUnit());
        record.setIntervals(vo.getIntervals());
        sendBatchSmsRecordRepository.save(record);
    }
}


