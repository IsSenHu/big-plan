package com.gapache.cloud.auth.server;

import com.gapache.security.annotation.EnableSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author HuSen
 * @since 2020/7/30 10:15 下午
 */
@EnableSecurity
@SpringBootApplication
public class AuthCenterServer {

    public static void main(String[] args) {
        SpringApplication.run(AuthCenterServer.class, args);
    }

}
