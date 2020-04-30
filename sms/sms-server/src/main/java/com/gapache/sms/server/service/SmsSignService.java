package com.gapache.sms.server.service;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/15 14:50
 */
public interface SmsSignService {

    void save(String signName, LocalDateTime createDate, String reason, Integer signStatus);
}
