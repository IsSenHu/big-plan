package com.gapache.security.checker.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.security.checker.SecurityChecker;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.security.model.AccessCard;
import com.gapache.security.model.AuthorizeInfoDTO;
import com.gapache.security.model.Certification;
import com.gapache.security.model.impl.CertificationImpl;
import com.gapache.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;

import java.security.PublicKey;
import java.util.HashSet;

/**
 * @author HuSen
 * @since 2020/7/31 12:45 下午
 */
@Slf4j
public class LocalSecurityChecker implements SecurityChecker {

    private final PublicKey publicKey;
    private final AuthorizeInfoManager authorizeInfoManager;

    public LocalSecurityChecker(PublicKey publicKey, AuthorizeInfoManager authorizeInfoManager) {
        this.publicKey = publicKey;
        this.authorizeInfoManager = authorizeInfoManager;
    }

    @Override
    public AccessCard checking(String token) {
        // 先解析并检查token
        try {
            String content = JwtUtils.parseToken(token, publicKey);
            log.info("parse token result:{}", content);
            Certification certification = JSON.parseObject(content, CertificationImpl.class);
            Long uniqueId = certification.getUniqueId();
            String name = certification.getName();
            AccessCard accessCard = new AccessCard();
            accessCard.setUserId(uniqueId);
            accessCard.setUsername(name);

            String info = authorizeInfoManager.get(token);
            AuthorizeInfoDTO authorizeInfoDTO = JSON.parseObject(info, AuthorizeInfoDTO.class);

            accessCard.setAuthorities(new HashSet<>(authorizeInfoDTO.getScopes()));
            accessCard.setCustomerInfo(authorizeInfoDTO.getCustomerInfo());
            return accessCard;
        } catch (Exception e) {
            log.error("check token error.", e);
        }
        return null;
    }
}
