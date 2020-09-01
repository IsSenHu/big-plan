package com.gapache.blog.admin;

import com.gapache.blog.sdk.annotation.EnableBlogServerFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author HuSen
 * create on 2020/4/5 04:13
 */
@EnableBlogServerFeign
@EnableDiscoveryClient
@SpringBootApplication
public class BlogAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogAdminApplication.class, args);
    }
}
