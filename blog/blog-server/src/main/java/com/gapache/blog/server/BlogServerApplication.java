package com.gapache.blog.server;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.gapache.blog.server.api.ScanBasePackageClasses;
import com.gapache.blog.server.lua.BlogLuaScript;
import com.gapache.blog.server.lua.ViewsLuaScript;
import com.gapache.redis.EnableRedis;
import com.gapache.redis.EnableRedisLua;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 目前操作都不能算是原子性的，因为缺少回滚的功能
 *
 * Dubbo
 * 超时时间，重试次数（不包含第一次调用）
 * 精确优先（方法级优先，接口级次之，全局配置再次之）
 * 消费者设置优先（如果级别一样，则消费方优先，提供方次之）
 *
 * @author HuSen
 * create on 2020/4/2 2:22 下午
 */
@EnableDubbo(scanBasePackageClasses = ScanBasePackageClasses.class)
@EnableRedis
@EnableRedisLua(value = {ViewsLuaScript.class, BlogLuaScript.class})
@SpringBootApplication
public class BlogServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogServerApplication.class, args);
    }
}
