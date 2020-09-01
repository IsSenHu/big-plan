package com.gapache.blog.server.lua;

import com.gapache.redis.LuaScript;

/**
 * @author HuSen
 * create on 2020/4/9 21:19
 */
public enum BlogLuaScript implements LuaScript {
    //
    CREATE("lua/createBlog.lua"),
    DELETE("lua/deleteBlog.lua"),
    UPDATE("lua/updateBlog.lua");

    BlogLuaScript(String path) {
        this.path = path;
    }

    private final String path;

    public static final String OK = "0";

    @Override
    public String path() {
        return this.path;
    }
}
