package com.gapache.sms.server.controller;

import com.gapache.commons.model.JsonResult;
import com.gapache.sms.server.model.*;
import com.gapache.sms.server.service.AliceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * create on 2020/1/14 11:46
 */
@RestController
@RequestMapping("/api/alice")
public class AliceController {

    private final AliceService aliceService;

    public AliceController(AliceService aliceService) {
        this.aliceService = aliceService;
    }

    @PostMapping("/sendSms")
    public JsonResult<String> sendSms(@RequestBody SendSmsVO vo) {
        return aliceService.sendSms(vo);
    }

    @PostMapping("/sendBatchSms")
    public JsonResult<String> sendBatchSms(@RequestBody SendBatchSmsVO vo) {
        return aliceService.sendBatchSms(vo);
    }

    @PostMapping("/addSmsTemplate")
    public JsonResult<String> addSmsTemplate(@RequestBody AddSmsTemplateVO vo) {
        return aliceService.addSmsTemplate(vo);
    }

    @PostMapping("/querySmsTemplate")
    public JsonResult<QuerySmsTemplateVO> querySmsTemplate(@RequestBody QuerySmsTemplateVO vo) {
        return aliceService.querySmsTemplate(vo);
    }

    @PostMapping("/addSmsSign")
    public JsonResult<String> addSmsSign(@RequestBody AddSmsSignVO vo) {
        return aliceService.addSmsSign(vo);
    }

    @PostMapping("/deleteSmsTemplate")
    public JsonResult<String> deleteSmsTemplate(@RequestBody DeleteSmsTemplateVO vo) {
        return aliceService.deleteSmsTemplate(vo);
    }

    @PostMapping("/querySmsSign")
    public JsonResult<QuerySmsSignVO> querySmsSign(@RequestBody QuerySmsSignVO vo) {
        return aliceService.querySmsSign(vo);
    }

    @PostMapping("/deleteSmsSign")
    public JsonResult<String> deleteSmsSign(@RequestBody DeleteSmsSignVO vo) {
        return aliceService.deleteSmsSign(vo);
    }

    @PostMapping("/modifySmsSign")
    public JsonResult<String> modifySmsSign(@RequestBody ModifySmsSignVO vo) {
        return aliceService.modifySmsSign(vo);
    }

    @PostMapping("/modifySmsTemplate")
    public JsonResult<String> modifySmsTemplate(@RequestBody ModifySmsTemplateVO vo) {
        return aliceService.modifySmsTemplate(vo);
    }

    @PostMapping("/querySendDetails")
    public JsonResult<SendDetailsVO> querySendDetails(@RequestBody QuerySendDetailsVO vo) {
        return aliceService.querySendDetails(vo);
    }
}
