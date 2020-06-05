package com.gapache.cloud.order;

import com.gapache.cloud.order.feign.PaymentFeign;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author HuSen
 * @since 2020/6/5 6:07 下午
 */
@EnableFeignClients
@SpringBootApplication
public class ConsumerOrderApplication {

    public static void main(String[] args) {
        System.out.println(SpringApplication.run(ConsumerOrderApplication.class, args).getBean(PaymentFeign.class).get(1L));
    }
}
