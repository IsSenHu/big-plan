package com.gapache.mybatis.demo.bean;

import com.gapache.mybatis.demo.dao.po.OrderJPO;
import com.gapache.mybatis.demo.dao.po.OrderPO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * create on 2020/5/14 6:17 下午
 */
@Configuration
public class DemoConfiguration {

    @Bean
    public OrderPO po() {
        System.out.println("po");
        return new OrderPO();
    }

    @Bean
    public OrderJPO jpo() {
        po();
        return new OrderJPO();
    }

}
