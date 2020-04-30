package com.gapache.sms.server;

import com.gapache.redis.EnableRedis;
import com.gapache.redis.EnableRedisLua;
import com.gapache.sms.server.alice.AliceLuaScript;
import com.gapache.sms.server.dao.repository.SmsSignRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HuSen
 * create on 2020/1/11
 */
@EnableRedis(enableTransactionSupport = true)
@EnableRedisLua(value = AliceLuaScript.class)
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }
}
