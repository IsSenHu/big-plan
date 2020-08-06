package com.gapache.security.checker.impl;

import com.gapache.security.checker.SecurityChecker;
import com.gapache.security.model.AccessCard;

import java.security.PublicKey;

/**
 * @author HuSen
 * @since 2020/7/31 12:45 下午
 */
public class LocalSecurityChecker implements SecurityChecker {

    private final PublicKey publicKey;

    public LocalSecurityChecker(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    @Override
    public AccessCard checking(String token) {
        return null;
    }
}
