package com.gapache.cloud.auth.server;

import com.gapache.security.annotation.EnableAuthResourceServer;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.netflix.config.DynamicStringProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HuSen
 * @since 2020/7/30 10:15 下午
 */
@Slf4j
@SpringBootApplication
@EnableDiscoveryClient
@EnableAuthResourceServer("AuthCenter")
public class AuthCenterServer {

    public static void main(String[] args) {
        SpringApplication.run(AuthCenterServer.class, args);

        DynamicPropertyFactory dynamicPropertyFactory = DynamicPropertyFactory.getInstance();
        DynamicIntProperty authCenterId = dynamicPropertyFactory
                .getIntProperty("auth.center.id", 0);
        DynamicStringProperty authCenterName = dynamicPropertyFactory
                .getStringProperty("auth.center.name", "not setting");
        log.info("{}:{} is started.", authCenterId.get(), authCenterName.get());
    }
}
