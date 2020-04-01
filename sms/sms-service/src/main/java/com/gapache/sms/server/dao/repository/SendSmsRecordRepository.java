package com.gapache.sms.server.dao.repository;

import com.gapache.sms.server.dao.po.SendSmsRecordPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author HuSen
 * create on 2020/1/14 15:01
 */
public interface SendSmsRecordRepository extends JpaRepository<SendSmsRecordPO, Long> {
}
