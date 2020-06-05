package com.gapache.cloud.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * JUC
 * 自旋 + CAS
 *
 * 指定某服务使用哪种轮训策略
 * /@RibbonClient(name = "CLOUD-PAYMENT-SERVICE", configuration = MySelfRule.class)/
 *
 * @author HuSen
 * @since 2020/6/2 5:41 下午
 */
@EnableDiscoveryClient
@SpringBootApplication
public class ConsumerOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerOrderApplication.class, args);
    }
}
