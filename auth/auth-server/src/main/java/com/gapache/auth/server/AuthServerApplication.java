package com.gapache.auth.server;

import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.gapache.auth.server.api.ScanBasePackageClasses;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HuSen
 * create on 2020/4/7 12:09 下午
 */
@EnableDubbo(scanBasePackageClasses = ScanBasePackageClasses.class)
@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class);
    }
}
