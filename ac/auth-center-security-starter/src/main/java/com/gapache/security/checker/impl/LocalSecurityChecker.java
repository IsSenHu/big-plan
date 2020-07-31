package com.gapache.security.checker.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.security.checker.SecurityChecker;
import com.gapache.security.exception.CertificationException;
import com.gapache.security.model.Certification;
import com.gapache.security.model.impl.CertificationImpl;
import com.gapache.security.utils.JwtUtils;
import io.jsonwebtoken.ExpiredJwtException;

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
    public Certification checking(String token) throws ExpiredJwtException, CertificationException {
        String content = JwtUtils.parseToken(token, publicKey);
        CertificationImpl certification = JSON.parseObject(content, CertificationImpl.class);
        if (certification == null) {
            throw new CertificationException("invalid token");
        }
        return certification;
    }
}
