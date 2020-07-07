package com.spring.demo.xml;

import com.spring.demo.xml.service.PetStoreService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author HuSen
 * @since 2020/7/7 11:48 下午
 */
public class Main {

    public static void main(String[] args) {
        // create and configure beans
        ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        // retrieve configured instance
        PetStoreService petStoreService = context.getBean("petStoreService", PetStoreService.class);

        // use configured instance
        petStoreService.buy(8888);
    }
}
