package com.gapache.cloud.money.management.server;

import com.gapache.security.annotation.EnableAuthResourceServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HuSen
 * @since 2020/8/10 11:14 上午
 */
@EnableDiscoveryClient
@EnableAuthResourceServer("MoneyManagement")
@SpringBootApplication
public class MoneyManagementServer {
    public static void main(String[] args) {
        SpringApplication.run(MoneyManagementServer.class, args);
    }
}
