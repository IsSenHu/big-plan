package com.gapache.sms.server.dao.repository;

import com.gapache.sms.server.dao.BaseJpaRepositoryBean;
import com.gapache.sms.server.dao.po.SmsSignPO;

import javax.persistence.EntityManager;

/**
 * @author HuSen
 * create on 2020/4/27 6:03 下午
 */
public class SmsSignRepositoryImpl extends BaseJpaRepositoryBean<SmsSignPO, Long> implements SmsSignDao {

    private final EntityManager em;

    public SmsSignRepositoryImpl(EntityManager em) {
        super(SmsSignPO.class, em);
        this.em = em;
    }

    @Override
    public SmsSignPO findBy() {
        System.out.println(1);
        return null;
    }
}
