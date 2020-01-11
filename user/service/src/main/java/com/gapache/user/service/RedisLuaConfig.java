package com.gapache.user.service;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * create on 2020/1/11 11:51
 */
@Configuration
@ConditionalOnBean(annotation = EnableLoadRedisLuaModule.class)
public class RedisLuaConfig {
}
