package com.gapache.blog.server.lua;

import com.gapache.redis.LuaScript;

/**
 * @author HuSen
 * create on 2020/4/4 22:54
 */
public enum CategoryLuaScript implements LuaScript {
    //
    INCREMENT("lua/incrementCategory.lua"),
    DECREMENT("lua/decrementCategory.lua");

    private String path;

    CategoryLuaScript(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return this.path;
    }
}
