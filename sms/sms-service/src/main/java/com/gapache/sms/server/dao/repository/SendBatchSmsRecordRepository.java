package com.gapache.sms.server.dao.repository;

import com.gapache.sms.server.dao.po.SendBatchSmsRecordPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author HuSen
 * create on 2020/1/14 17:05
 */
public interface SendBatchSmsRecordRepository extends JpaRepository<SendBatchSmsRecordPO, Long> {
}
