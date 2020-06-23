package com.gapache.oms.store.location.sdk.config;

import com.gapache.oms.store.location.sdk.annotation.EnableAreaFeign;
import com.gapache.oms.store.location.sdk.fallback.AreaFeignFallback;
import feign.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableFeignClients(basePackages = "com.gapache.oms.store.location.sdk.feign")
@ConditionalOnBean(annotation = EnableAreaFeign.class)
@Import(AreaFeignFallback.class)
public class AreaFeignAutoConfiguration {

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
