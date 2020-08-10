package com.spring.demo.java;

import com.spring.demo.java.dao.Cat;
import com.spring.demo.java.dao.CatRepository;
import com.spring.demo.java.service.TestService;
import org.apache.logging.log4j.LogManager;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.StringQuery;
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

        ElasticsearchOperations elasticsearchOperations = applicationContext.getBean(ElasticsearchOperations.class);
//        for (int i = 0; i < 100; i++) {
//            Cat cat = new Cat();
//            cat.setName("husen" + i);
//            cat.setAge(i + 1);
//            Cat save = elasticsearchOperations.save(cat);
//            System.out.println(save);
//        }

        CatRepository catRepository = applicationContext.getBean(CatRepository.class);
        Cat husen = catRepository.findByName("husen");
        System.out.println(husen);

        Page<Cat> catPage = catRepository.searchSimilar(husen, new String[]{"name.last"}, PageRequest.of(0, 10));


        System.out.println(catPage.getContent());

        SearchHits<Cat> search = elasticsearchOperations.search(new StringQuery(""), Cat.class);
        for (SearchHit<Cat> searchHit : search.getSearchHits()) {
            searchHit.getContent();
        }

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
