package com.gapache.sms.server.service;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/15 14:38
 */
public interface SmsTemplateService {

    void save(String templateCode, String templateName, String templateContent, Integer templateStatus, Integer templateType, String reason, LocalDateTime createDate);
}
