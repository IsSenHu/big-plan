package com.gapache.oms.split.order.server.lua;

import com.gapache.redis.LuaScript;

/**
 * 订单LUA脚本
 *
 * @author HuSen
 * @since 2020/6/22 6:09 下午
 */
public enum OrderLuaScript implements LuaScript {
    // 检查订单号唯一性
    CHECK_ORDER_UNIQUE("lua/checkOrderUnique.lua");

    private final String path;

    OrderLuaScript(String path) {
        this.path = path;
    }

    @Override
    public String path() {
        return this.path;
    }
}
