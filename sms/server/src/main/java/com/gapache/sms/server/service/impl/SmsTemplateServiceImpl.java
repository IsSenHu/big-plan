package com.gapache.sms.server.service.impl;

import com.gapache.sms.server.alice.TemplateType;
import com.gapache.sms.server.dao.po.SmsTemplatePO;
import com.gapache.sms.server.dao.repository.SmsTemplateRepository;
import com.gapache.sms.server.service.SmsTemplateService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/15 14:38
 */
@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {

    private final SmsTemplateRepository smsTemplateRepository;

    public SmsTemplateServiceImpl(SmsTemplateRepository smsTemplateRepository) {
        this.smsTemplateRepository = smsTemplateRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String templateCode, String templateName, String templateContent, Integer templateStatus, Integer templateType, String reason, LocalDateTime createDate) {
        SmsTemplatePO old = smsTemplateRepository.findByTemplateCode(templateCode);
        if (old == null) {
            old = new SmsTemplatePO();
        }
        old.setTemplateCode(templateCode);
        old.setTemplateName(templateName);
        old.setTemplateContent(templateContent);
        old.setTemplateStatus(templateStatus);
        old.setTemplateType(TemplateType.fromValue(templateType));
        old.setReason(reason);
        old.setCreateDate(createDate);
        smsTemplateRepository.save(old);
    }
}
