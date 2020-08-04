package com.gapache.security.model;

import java.io.Serializable;
import java.security.Principal;

/**
 * @author HuSen
 * @since 2020/7/31 12:42 下午
 */
public interface Certification extends Principal, Serializable {

    /**
     * 凭证对应的唯一ID
     *
     * @return 唯一ID
     */
    Long getUniqueId();

    /**
     * 自定义信息（扩展用，提高灵活性）
     *
     * @return 自定义信息
     */
    CustomerInfo getCustomerInfo();
}
