package com.gapache.sms.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.redis.RedisLuaExecutor;
import com.gapache.sms.server.alice.AliceLuaScript;
import com.gapache.sms.server.alice.SMSAlice;
import com.gapache.sms.server.alice.TemplateType;
import com.gapache.sms.server.alice.request.impl.*;
import com.gapache.sms.server.alice.response.impl.*;
import com.gapache.sms.server.dao.po.SendBatchSmsRecordPO;
import com.gapache.sms.server.dao.po.SendSmsRecordPO;
import com.gapache.sms.server.dao.po.SmsSignPO;
import com.gapache.sms.server.dao.po.SmsTemplatePO;
import com.gapache.sms.server.dao.repository.SendBatchSmsRecordRepository;
import com.gapache.sms.server.dao.repository.SendSmsRecordRepository;
import com.gapache.sms.server.dao.repository.SmsSignRepository;
import com.gapache.sms.server.dao.repository.SmsTemplateRepository;
import com.gapache.sms.server.model.*;
import com.gapache.sms.server.service.AliceService;
import com.gapache.sms.server.service.SmsSignService;
import com.gapache.sms.server.service.SmsTemplateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * create on 2020/1/14 11:47
 */
@Service
public class AliceServiceImpl implements AliceService {

    private final SMSAlice smsAlice;
    private final SendSmsRecordRepository sendSmsRecordRepository;
    private final SendBatchSmsRecordRepository sendBatchSmsRecordRepository;
    private final SmsTemplateRepository smsTemplateRepository;
    private final SmsSignRepository smsSignRepository;

    private final SmsTemplateService smsTemplateService;
    private final SmsSignService smsSignService;

    private final RedisLuaExecutor luaExecutor;

    public AliceServiceImpl(SMSAlice smsAlice, SendSmsRecordRepository sendSmsRecordRepository, SendBatchSmsRecordRepository sendBatchSmsRecordRepository, SmsTemplateRepository smsTemplateRepository, SmsSignRepository smsSignRepository, SmsTemplateService smsTemplateService, SmsSignService smsSignService, RedisLuaExecutor luaExecutor) {
        this.smsAlice = smsAlice;
        this.sendSmsRecordRepository = sendSmsRecordRepository;
        this.sendBatchSmsRecordRepository = sendBatchSmsRecordRepository;
        this.smsTemplateRepository = smsTemplateRepository;
        this.smsSignRepository = smsSignRepository;
        this.smsTemplateService = smsTemplateService;
        this.smsSignService = smsSignService;
        this.luaExecutor = luaExecutor;
    }

    @Override
    public JsonResult<String> sendSms(SendSmsVO vo) {
        SmsSignPO sign = smsSignRepository.findBySignName(vo.getSignName());
        ThrowUtils.throwIfTrue(sign == null, AliceError.SIGN_NOT_EXISTED);

        SmsTemplatePO template = smsTemplateRepository.findByTemplateCode(vo.getTemplateCode());
        ThrowUtils.throwIfTrue(template == null, AliceError.TEMPLATE_NOT_EXISTED);

        String code = null;
        boolean isCode = template.getTemplateType() == TemplateType._0;

        String[] phoneNumbers = vo.getPhoneNumbers().split(",");
        ThrowUtils.throwIfTrue(isCode && phoneNumbers.length > 1, AliceError.CODE_ALWAYS_SINGLE);

        if (isCode) {
            String codeField = StringUtils.substringBetween(template.getTemplateContent(), "${", "}");
            JSONObject json = JSON.parseObject(vo.getTemplateParam());
            ThrowUtils.throwIfTrue(!json.containsKey(codeField), AliceError.NOT_HAVE_CODE);
            code = json.getString(codeField);
            ThrowUtils.throwIfTrue(StringUtils.isBlank(code), AliceError.NOT_HAVE_CODE);
        }

        TimeUnit timeUnit = vo.getTimeUnit();
        List<String> willSend = new ArrayList<>(phoneNumbers.length);
        for (String phoneNumber : phoneNumbers) {
            // 这里需要一个事务的操作 用lua脚本实现 过滤掉不能发送短信的手机号
            String result = luaExecutor.execute(
                    AliceLuaScript.SEND_SMS,
                    new ArrayList<>(0),
                    phoneNumber,
                    vo.getTemplateCode(),
                    vo.getSignName(),
                    String.valueOf(timeUnit.toSeconds(vo.getEffectiveTime())),
                    String.valueOf(timeUnit.toSeconds(vo.getIntervals())),
                    String.valueOf(template.getTemplateType().getValue()),
                    code
            );

            ThrowUtils.throwIfTrue(isCode && "1".equals(result), AliceError.SEND_FREQUENTLY);

            if ("0".equals(result)) {
                willSend.add(phoneNumber);
            }
        }
        String willSendNumbers = StringUtils.join(willSend.toArray(new String[0]), ",");
        vo.setPhoneNumbers(willSendNumbers);

        SendSmsRequest request = new SendSmsRequest(
                smsAlice,
                vo.getPhoneNumbers(),
                vo.getSignName(),
                vo.getTemplateCode(),
                vo.getSmsUpExtendCode(),
                vo.getTemplateParam(),
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

        smsTemplateService.save(
                vo.getTemplateCode(),
                response.getTemplateName(),
                response.getTemplateContent(),
                response.getTemplateStatus(),
                response.getTemplateType(),
                response.getReason(),
                response.getCreateDate()
        );

        vo.setCreateDate(response.getCreateDate());
        vo.setReason(response.getReason());
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

        smsSignService.save(
                vo.getSignName(),
                response.getCreateDate(),
                response.getReason(),
                response.getSignStatus()
        );

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
            record.setCode(response.getCode());
            record.setBizId(response.getBizId());
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
        record.setTemplateParam(vo.getTemplateParam());
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


