package com.gapache.redis;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/1/15 12:00
 */
public interface LuaScript {
    String prefixPath();
    String luaScriptName();
    List<LuaScript> all();
}
