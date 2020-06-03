package com.gapache.cloud.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author HuSen
 * @since 2020/6/3 4:15 下午
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * 必须开启负载均衡，负责不能通过服务名称去访问服务
     *
     * @see org.springframework.cloud.client.loadbalancer.LoadBalanced
     * @return RestTemplate
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
