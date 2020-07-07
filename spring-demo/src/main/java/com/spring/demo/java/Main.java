package com.spring.demo.java;

import com.spring.demo.java.service.TestService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * @since 2020/7/7 5:38 下午
 */
@Configuration
@ComponentScan({"com.spring.demo"})
public class Main {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);
        applicationContext.getBean(TestService.class).test();
    }
}
