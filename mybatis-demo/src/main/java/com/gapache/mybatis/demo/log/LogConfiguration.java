package com.gapache.mybatis.demo.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

/**
 * spring4和spring5都使用jcl
 * spring4当中依赖commons.logging
 * spring5中使用的spring.jcl 改过的jcl
 *
 * jcl他不直接记录日志，他是通过第三方记录日志
 * slf4j 绑定与桥接
 * 具体实现：
 * jul jdk的
 * log4j
 * log4j2
 * logback 直接实现了slf4j的api
 *
 * @author HuSen
 * create on 2020/5/15 2:03 下午
 */
@Configuration
public class LogConfiguration {

    @Bean
    public Object object() {
        // 用jdk的
        Logger logger = Logger.getLogger("神奇的log------------test");
        logger.info("some");
        return new Object();
    }
}
