package com.gapache.blog.server;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.gapache.blog.server.api.ScanBasePackageClasses;
import com.gapache.blog.server.lua.CategoryLuaScript;
import com.gapache.blog.server.lua.TagLuaScript;
import com.gapache.redis.EnableRedis;
import com.gapache.redis.EnableRedisLua;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HuSen
 * create on 2020/4/2 2:22 下午
 */
@EnableDubbo(scanBasePackageClasses = ScanBasePackageClasses.class)
@EnableRedis
@EnableRedisLua(value = {TagLuaScript.class, CategoryLuaScript.class})
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
