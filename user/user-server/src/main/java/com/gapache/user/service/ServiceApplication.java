package com.gapache.user.service;

import com.gapache.redis.EnableRedis;
import com.gapache.uid.annotation.EnableUidGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HuSen
 * create on 2020/1/8 18:01
 */
@EnableRedis(enableTransactionSupport = true)
@EnableUidGenerator
@SpringBootApplication
public class ServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }
}
