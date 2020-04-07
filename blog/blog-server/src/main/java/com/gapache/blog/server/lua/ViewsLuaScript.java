package com.gapache.blog.server.lua;

import com.gapache.redis.LuaScript;

/**
 * @author HuSen
 * create on 2020/4/5 21:11
 */
public enum  ViewsLuaScript implements LuaScript {
    //
    INCREMENT("lua/incrementViews.lua");

    private String path;

    ViewsLuaScript(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return this.path;
    }
}
