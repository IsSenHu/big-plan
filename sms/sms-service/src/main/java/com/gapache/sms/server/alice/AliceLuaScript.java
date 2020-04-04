package com.gapache.sms.server.alice;

import com.gapache.redis.LuaScript;

/**
 * @author HuSen
 * create on 2020/1/15 14:07
 */
public enum AliceLuaScript implements LuaScript {
    //
    SEND_SMS("lua/sendSms.lua");

    private String path;

    AliceLuaScript(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return this.path;
    }
}
