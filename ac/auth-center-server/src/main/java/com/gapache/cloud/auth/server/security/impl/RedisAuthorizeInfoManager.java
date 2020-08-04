package com.gapache.cloud.auth.server.security.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.model.AuthorizeInfoDTO;
import com.gapache.cloud.auth.server.security.AuthorizeInfoManager;
import com.gapache.security.model.CustomerInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * @since 2020/8/4 6:19 下午
 */
public class RedisAuthorizeInfoManager implements AuthorizeInfoManager {

    private final StringRedisTemplate stringRedisTemplate;

    public RedisAuthorizeInfoManager(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public void save(String token, Long timeout, CustomerInfo customerInfo, List<String> scopes) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        AuthorizeInfoDTO authorizeInfoDTO = new AuthorizeInfoDTO();
        authorizeInfoDTO.setCustomerInfo(customerInfo);
        authorizeInfoDTO.setScopes(scopes);
        opsForValue.set(token, JSON.toJSONString(authorizeInfoDTO), timeout, TimeUnit.MILLISECONDS);
    }

    @Override
    public void delete(String token) {
        stringRedisTemplate.delete(token);
    }
}
