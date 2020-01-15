package com.gapache.redis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.lang.NonNull;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * create on 2019/12/5 11:37
 */
@Slf4j
public class LuaScriptMap {

    private static final Map<String, RedisScript<String>> REDIS_SCRIPT_MAP = new HashMap<>(12);

    protected void addLuaScript(String prefixPath, String luaScriptName) {
        if (StringUtils.isBlank(luaScriptName)) {
            log.warn("luaScriptName is blank!");
            return;
        }
        if (REDIS_SCRIPT_MAP.containsKey(luaScriptName)) {
            log.warn("luaScrip:[{}] is existed!", luaScriptName);
            return;
        }
        String fullPath = toFullPath(prefixPath, luaScriptName);
        ClassPathResource resource = new ClassPathResource(fullPath);
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            RedisScript<String> redisScript = new DefaultRedisScript<>(new String(bytes, StandardCharsets.UTF_8), String.class);
            REDIS_SCRIPT_MAP.putIfAbsent(fullPath, redisScript);
        } catch (Exception e) {
            log.error("[{}] luaScript is load fail:", luaScriptName, e);
        }
    }

    @NonNull
    public RedisScript<String> getLuaScript(String prefixPath, String luaScriptName) {
        String fullPath = toFullPath(prefixPath, luaScriptName);
        return REDIS_SCRIPT_MAP.get(fullPath);
    }

    private String toFullPath(String prefixPath, String luaScriptName) {
        return StringUtils.isBlank(prefixPath) ? luaScriptName : prefixPath.concat(File.separator).concat(luaScriptName);
    }
}
