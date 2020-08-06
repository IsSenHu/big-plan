package com.gapache.security.model;

import lombok.Data;

import java.util.Set;

/**
 * @author HuSen
 * @since 2020/8/5 11:07 上午
 */
@Data
public class AccessCard {
    private Long userId;
    private String username;
    private CustomerInfo customerInfo;
    private Set<String> authorities;
}
