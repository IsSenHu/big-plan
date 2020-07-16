package com.spring.demo.xml.factory;

import com.spring.demo.xml.service.MoneyService;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author HuSen
 * @since 2020/7/11 5:08 下午
 */
public class MoneyServiceFactoryBean implements FactoryBean<MoneyService> {

    @Override
    public MoneyService getObject() {
        return new MoneyService();
    }

    @Override
    public Class<?> getObjectType() {
        return MoneyService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
