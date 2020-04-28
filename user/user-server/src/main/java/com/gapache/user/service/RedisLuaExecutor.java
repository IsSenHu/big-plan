package com.gapache.user.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * @author HuSen
 * create on 2019/12/5 11:32
 */
@Slf4j
public class RedisLuaExecutor {

    private final StringRedisTemplate template;

    public RedisLuaExecutor(StringRedisTemplate template) {
        this.template = template;
    }

    public Long execute(LuaScriptMap luaScriptMap, List<String> keys, Object... args) {
//        if (log.isDebugEnabled()) {
//            log.debug("redis 执行 lua 脚本:{}, keys:{}, args:{}", luaScriptMap.getLuaScriptName(), keys, Arrays.toString(args));
//        }
//        return template.execute(luaScriptMap.getRedisScript(), keys, args);
        return 0L;
    }
}
