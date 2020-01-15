package com.gapache.sms.server.alice;

import com.gapache.redis.LuaScript;

/**
 * @author HuSen
 * create on 2020/1/15 14:07
 */
public enum AliceLuaScript implements LuaScript {
    SEND_SMS("lua", "sendSms.lua");

    private String prefixPath;
    private String luaScriptName;

    AliceLuaScript(String prefixPath, String luaScriptName) {
        this.prefixPath = prefixPath;
        this.luaScriptName = luaScriptName;
    }

    @Override
    public String prefixPath() {
        return this.prefixPath;
    }

    @Override
    public String luaScriptName() {
        return this.luaScriptName;
    }
}
