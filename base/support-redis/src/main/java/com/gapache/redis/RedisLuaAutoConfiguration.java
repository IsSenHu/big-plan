package com.gapache.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/1/11 11:51
 */
@Slf4j
@ConditionalOnBean(annotation = EnableRedisLua.class)
public class RedisLuaAutoConfiguration {

    private final ApplicationContext applicationContext;

    public RedisLuaAutoConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public RedisLuaExecutor luaExecutor(StringRedisTemplate redisTemplate) {
        LuaScriptMap luaScriptMap = new LuaScriptMap();
        Map<String, Object> enableRedisLuaOnClass = applicationContext.getBeansWithAnnotation(EnableRedisLua.class);
        if (MapUtils.isNotEmpty(enableRedisLuaOnClass)) {
            EnableRedisLua enableRedisLua = AnnotationUtils.findAnnotation(enableRedisLuaOnClass.values().iterator().next().getClass(), EnableRedisLua.class);
            if (null != enableRedisLua) {
                for (Class<? extends LuaScript> aClass : enableRedisLua.value()) {
                    try {
                        LuaScript luaScript = aClass.newInstance();
                        for (LuaScript script : luaScript.all()) {
                            luaScriptMap.addLuaScript(script.prefixPath(), script.luaScriptName());
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        log.error("Load luaScript error.", e);
                    }
                }
            }
        }
        return new RedisLuaExecutor(redisTemplate, luaScriptMap);
    }
}
