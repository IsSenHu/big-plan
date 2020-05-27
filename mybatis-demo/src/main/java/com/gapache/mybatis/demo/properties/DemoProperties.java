package com.gapache.mybatis.demo.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author HuSen
 * @since 2020/5/25 11:02 上午
 */
@Data
@PropertySource("classpath:/demo.properties")
@Configuration
@ConfigurationProperties(prefix = "spring.demo")
public class DemoProperties {

    private String name;
}
