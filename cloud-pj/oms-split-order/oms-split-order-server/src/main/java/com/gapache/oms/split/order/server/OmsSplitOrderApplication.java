package com.gapache.oms.split.order.server;

import com.gapache.redis.EnableRedisLua;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HuSen
 * @since 2020/6/22 10:10 上午
 */
@EnableRedisLua
@SpringBootApplication
public class OmsSplitOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmsSplitOrderApplication.class, args);
    }
}
