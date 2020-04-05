package com.gapache.blog.server.lua;

import com.gapache.redis.LuaScript;

/**
 * @author HuSen
 * create on 2020/4/4 13:26
 */
public enum TagLuaScript implements LuaScript {
    //
    INCREMENT("lua/incrementTag.lua"),
    DECREMENT("lua/decrementTag.lua"),
    DECREMENT_DECREMENT("lua/decrementThenIncrementTag.lua");

    private String path;

    TagLuaScript(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return this.path;
    }
}
