package com.gapache.blog.sdk.config;

import com.gapache.blog.sdk.annotation.EnableBlogServerFeign;
import com.gapache.blog.sdk.fallback.BlogServerFeignFallback;
import feign.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author HuSen
 * @since 2020/8/26 9:44 上午
 */
@Configuration
@EnableFeignClients(basePackages = "com.gapache.blog.sdk.feign")
@ConditionalOnBean(annotation = EnableBlogServerFeign.class)
@Import(BlogServerFeignFallback.class)
public class BlogServerFeignAutoConfiguration {

    /**
     * 支持Feign的日志，但是要在yml里面配置日志级别才能打印
     *
     * @return Feign的Full日志
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
