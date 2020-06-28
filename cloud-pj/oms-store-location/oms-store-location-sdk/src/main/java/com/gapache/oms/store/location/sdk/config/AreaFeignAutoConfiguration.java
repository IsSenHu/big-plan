package com.gapache.oms.store.location.sdk.config;

import com.gapache.oms.store.location.sdk.annotation.EnableStoreLocationFeign;
import com.gapache.oms.store.location.sdk.fallback.StoreLocationFeignFallback;
import feign.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Husen
 * @since 2020/06/23 23:59:59
 */
@Configuration
@EnableFeignClients(basePackages = "com.gapache.oms.store.location.sdk.feign")
@ConditionalOnBean(annotation = EnableStoreLocationFeign.class)
@Import(StoreLocationFeignFallback.class)
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
