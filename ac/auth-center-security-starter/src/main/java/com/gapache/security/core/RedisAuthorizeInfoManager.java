package com.gapache.security.core;

import com.alibaba.fastjson.JSON;
import com.gapache.security.interfaces.AuthorizeInfoManager;
import com.gapache.security.model.AuthorizeInfoDTO;
import com.gapache.security.model.CustomerInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Collection;
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
    public void save(String token, Long timeout, CustomerInfo customerInfo, Collection<String> scopes) {
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

    @Override
    public String get(String token) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        return opsForValue.get(token);
    }
}
