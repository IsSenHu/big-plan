package com.gapache.security.config;

import com.gapache.commons.security.RSAUtils;
import com.gapache.security.checker.SecurityChecker;
import com.gapache.security.checker.impl.LocalSecurityChecker;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.security.core.RedisAuthorizeInfoManager;
import com.gapache.security.properties.SecurityProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

/**
 * @author HuSen
 * @since 2020/7/31 1:14 下午
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {

    private final SecurityProperties securityProperties;

    public SecurityAutoConfiguration(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @ConditionalOnProperty("com.gapache.security.public-key")
    @Bean("localSecurityChecker")
    public SecurityChecker localSecurityChecker(AuthorizeInfoManager authorizeInfoManager) throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        log.info("启用公钥解密############");
        return new LocalSecurityChecker(RSAUtils.getPublicKey(securityProperties.getPublicKey().trim().replaceAll(" ", "")), authorizeInfoManager);
    }

    @Bean
    @ConditionalOnProperty("com.gapache.security.private-key")
    public PrivateKey privateKey() throws NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        log.info("启用私钥进行签名############");
        return RSAUtils.getPrivateKey(securityProperties.getPrivateKey().trim().replaceAll(" ", ""));
    }

    @Bean
    public AuthorizeInfoManager authorizeInfoManager(StringRedisTemplate stringRedisTemplate) {
        return new RedisAuthorizeInfoManager(stringRedisTemplate);
    }
}
