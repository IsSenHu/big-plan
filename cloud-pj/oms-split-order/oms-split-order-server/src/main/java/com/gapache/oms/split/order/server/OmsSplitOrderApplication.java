package com.gapache.oms.split.order.server;

import com.gapache.oms.split.order.server.lua.OrderLuaScript;
import com.gapache.oms.store.location.sdk.annotation.EnableStoreLocationFeign;
import com.gapache.redis.EnableRedisLua;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HuSen
 * @since 2020/6/22 10:10 上午
 */
@EnableDiscoveryClient
@EnableStoreLocationFeign
@EnableRedisLua({OrderLuaScript.class})
@SpringBootApplication
public class OmsSplitOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmsSplitOrderApplication.class, args);
    }
}
