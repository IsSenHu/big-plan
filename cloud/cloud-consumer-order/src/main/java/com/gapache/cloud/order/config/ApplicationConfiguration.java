package com.gapache.cloud.order.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Ribbon是客户端负载均衡的工具，主要是提供软件负载均衡算法和服务调用
 *
 * @author HuSen
 * @since 2020/6/2 5:48 下午
 */
@Configuration
public class ApplicationConfiguration {

    /**
     * 必须开启负载均衡，否则不能通过服务名称去访问服务
     * 新版：引入EurekaClient的时候，就引入了Ribbon
     * @see com.netflix.loadbalancer.IRule
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
