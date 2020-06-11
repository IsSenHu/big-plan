package com.gapache.cloud.stream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HuSen
 * @since 2020/6/11 5:45 下午
 */
@EnableDiscoveryClient
@SpringBootApplication
public class StreamRabbitmqProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(StreamRabbitmqProviderApplication.class, args);
    }
}
