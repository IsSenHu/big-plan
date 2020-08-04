package com.gapache.security.model.impl;

import com.gapache.security.model.Certification;
import com.gapache.security.model.CustomerInfo;
import lombok.Setter;
import lombok.ToString;

/**
 * @author HuSen
 * @since 2020/7/31 1:37 下午
 */
@Setter
@ToString
public class CertificationImpl implements Certification {
    private static final long serialVersionUID = 511998569184094424L;

    private Long id;

    private CustomerInfo customerInfo;

    private String name;

    @Override
    public Long getUniqueId() {
        return this.id;
    }

    @Override
    public CustomerInfo getCustomerInfo() {
        return this.customerInfo;
    }

    @Override
    public String getName() {
        return this.name;
    }
}
