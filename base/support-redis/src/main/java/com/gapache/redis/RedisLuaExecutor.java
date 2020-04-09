package com.gapache.redis;

import com.gapache.commons.utils.IStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2019/12/5 11:32
 */
@Slf4j
public class RedisLuaExecutor {

    private final RedisTemplate<byte[], byte[]> template;
    private final LuaScriptMap luaScriptMap;

    public RedisLuaExecutor(RedisTemplate<byte[], byte[]> template, LuaScriptMap luaScriptMap) {
        this.template = template;
        this.luaScriptMap = luaScriptMap;
    }

    public byte[] execute(LuaScript script, List<String> keys, Object... args) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("redis 执行 lua 脚本:{}, keys:{}, args:{}", script.path(), keys, Arrays.toString(args));
            }
            List<byte[]> keyBytesList = keys.stream().map(IStringUtils::getBytes).collect(Collectors.toList());
            return template.execute(luaScriptMap.getLuaScript(script.path()), keyBytesList, args);
        } catch (Exception e) {
            log.error("Execute Lua Script error.", e);
            return IStringUtils.getBytes("9999");
        }
    }
}
