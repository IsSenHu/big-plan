package com.gapache.sms.server.dao.repository;

import com.gapache.sms.server.dao.BaseJpaRepository;
import com.gapache.sms.server.dao.po.SmsSignPO;

/**
 * @author HuSen
 * create on 2020/1/15 14:50
 */
public interface SmsSignRepository extends BaseJpaRepository<SmsSignPO, Long>, SmsSignDao {

    SmsSignPO findBySignName(String signName);
}
