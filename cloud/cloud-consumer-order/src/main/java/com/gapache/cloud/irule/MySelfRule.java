package com.gapache.cloud.irule;

import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RandomRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * @since 2020/6/4 11:14 上午
 */
@Configuration
public class MySelfRule {

    @Bean
    public IRule iRule() {
        return new RandomRule();
    }
}
