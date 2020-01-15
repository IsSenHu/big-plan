package com.gapache.sms.server.service.impl;

import com.gapache.sms.server.dao.po.SmsSignPO;
import com.gapache.sms.server.dao.repository.SmsSignRepository;
import com.gapache.sms.server.service.SmsSignService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/15 14:50
 */
@Service
public class SmsSignServiceImpl implements SmsSignService {

    private final SmsSignRepository smsSignRepository;

    public SmsSignServiceImpl(SmsSignRepository smsSignRepository) {
        this.smsSignRepository = smsSignRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String signName, LocalDateTime createDate, String reason, Integer signStatus) {
        SmsSignPO old = smsSignRepository.findBySignName(signName);
        if (old == null) {
            old = new SmsSignPO();
        }
        old.setSignName(signName);
        old.setCreateDate(createDate);
        old.setReason(reason);
        old.setSignStatus(signStatus);
        smsSignRepository.save(old);
    }
}
