package com.gapache.redis;

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
    private final LuaScriptMap luaScriptMap;

    public RedisLuaExecutor(StringRedisTemplate template, LuaScriptMap luaScriptMap) {
        this.template = template;
        this.luaScriptMap = luaScriptMap;
    }

    public String execute(LuaScript script, List<String> keys, Object... args) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("redis 执行 lua 脚本:{}, keys:{}, args:{}", script.path(), keys, Arrays.toString(args));
            }
            return template.execute(luaScriptMap.getLuaScript(script.path()), keys, args);
        } catch (Exception e) {
            log.error("Execute Lua Script error.", e);
            return "9999";
        }
    }
}
