package com.gapache.oms.pull.order.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author HuSen
 * @since 2020/6/18 5:24 下午
 */
@EnableScheduling
@SpringBootApplication
public class OmsPullOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmsPullOrderApplication.class, args);
    }
}
