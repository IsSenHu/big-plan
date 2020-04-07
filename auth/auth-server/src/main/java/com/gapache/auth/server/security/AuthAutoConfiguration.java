package com.gapache.auth.server.security;

import com.gapache.auth.sdk.JwtManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * create on 2020/4/7 12:44 下午
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(AuthProperties.class)
public class AuthAutoConfiguration {

    private final AuthProperties authProperties;

    public AuthAutoConfiguration(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    @Bean
    public JwtManager jwtManager() {
        return new JwtManager(authProperties.getSecret(), authProperties.getExpire());
    }
}
