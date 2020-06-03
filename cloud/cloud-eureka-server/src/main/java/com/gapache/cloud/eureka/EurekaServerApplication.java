package com.gapache.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 短时间内，服务大量下线，EurekaServer就会进入自我保护模式。
 * 自我保护：
 *  某时刻某一个微服务不可用了，Eureka不会立刻清理，依旧会对该微服务的信息进行保存
 *  属于CAP理论中的AP
 * 为什么会产生Eureka自我保护机制？
 *  为了防止，EurekaClient可以正常运行，但是与EurekaServer网络不通，EurekaServer立刻将EurekaClient服务剔除情况发生。
 * 设计哲学就是宁可保留错误的服务注册信息，也不盲目注销任何可能健康的服务实例
 *
 * @author HuSen
 * @since 2020/6/3 11:47 上午
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
