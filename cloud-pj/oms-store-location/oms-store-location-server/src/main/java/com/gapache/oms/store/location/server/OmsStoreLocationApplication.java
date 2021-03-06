package com.gapache.oms.store.location.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HuSen
 * @since 2020/6/23 11:30 上午
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OmsStoreLocationApplication {

    public static void main(String[] args) {
        SpringApplication.run(OmsStoreLocationApplication.class, args);
    }
}
