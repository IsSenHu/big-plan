package com.gapache.cloud.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HuSen
 * create on 2020/6/9 00:25
 */
@EnableCircuitBreaker
@EnableDiscoveryClient
@SpringBootApplication
public class ProviderPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProviderPaymentApplication.class, args);
    }
}
