package com.spring.demo.java;

import com.spring.demo.java.service.TestService;
import org.apache.logging.log4j.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.util.logging.Logger;

/**
 * @author HuSen
 * @since 2020/7/7 5:38 下午
 */
@EnableElasticsearchRepositories
@Configuration
@ComponentScan({"com.spring.demo"})
public class Main {

    public static void main(String[] args) {
        // 桥接其他日志到slf4j
        bridgeOtherToSlf4j();

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Main.class);
        applicationContext.getBean(TestService.class).test();

        // 手动注册单例对象
        manualRegisterBean(applicationContext);

        // 测试jul桥接到slf4j
        useJul();
        // 测试log4j2桥接到slf4j
        useLog4j2();
    }

    private static void manualRegisterBean(AnnotationConfigApplicationContext applicationContext) {
        ConfigurableListableBeanFactory beanFactory = applicationContext.getBeanFactory();
        beanFactory.registerSingleton("manualRegisterTestService", new TestService() {
            @Override
            public void test() {
                System.out.println("manualRegisterTestService");
            }

            @Override
            public void testInnerAspectJ() {
                System.out.println("manualRegisterTestService");
            }
        });
        applicationContext.getBean("manualRegisterTestService", TestService.class).test();
    }

    private static void useLog4j2() {
        org.apache.logging.log4j.Logger log4j2 = LogManager.getLogger("log4j2");
        log4j2.info("233333");
    }

    private static void useJul() {
        // 使用jul
        Logger logger = Logger.getLogger("jul");
        logger.info("233333");
    }

    private static void bridgeOtherToSlf4j() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}
