package com.gapache.sms.server.dao.repository;

import com.gapache.sms.server.dao.po.SmsSignPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author HuSen
 * create on 2020/1/15 14:50
 */
public interface SmsSignRepository extends JpaRepository<SmsSignPO, Long> {

    SmsSignPO findBySignName(String signName);
}
