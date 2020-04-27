package com.gapache.sms.server.dao;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;

/**
 * @author HuSen
 * create on 2020/4/27 5:46 下午
 */
@NoRepositoryBean
public abstract class BaseJpaRepositoryBean<T, ID> extends SimpleJpaRepository<T, ID> {

    public BaseJpaRepositoryBean(Class<T> tClass, EntityManager entityManager) {
        super(tClass, entityManager);
    }
}
