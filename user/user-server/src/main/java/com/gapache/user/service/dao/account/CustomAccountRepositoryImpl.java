package com.gapache.user.service.dao.account;


import com.gapache.jpa.BaseJpaRepositoryBean;

import javax.persistence.EntityManager;

/**
 * @author HuSen
 * create on 2020/4/28 11:07 上午
 */
public class CustomAccountRepositoryImpl extends BaseJpaRepositoryBean<AccountPO, Long> implements CustomAccountRepository {

    private final EntityManager em;

    public CustomAccountRepositoryImpl(EntityManager entityManager) {
        super(AccountPO.class, entityManager);
        this.em = entityManager;
    }

    @Override
    public void test() {
        System.out.println("test");
    }
}
