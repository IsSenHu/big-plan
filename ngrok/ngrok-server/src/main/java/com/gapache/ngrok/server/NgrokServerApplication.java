package com.gapache.ngrok.server;

import com.gapache.ngrok.server.http.HttpServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author HuSen
 * create on 2020/4/11 4:26 下午
 */
@Slf4j
@SpringBootApplication
public class NgrokServerApplication {

    @Bean
    public HttpServer server() {
        HttpServer server = new HttpServer(7777, "ngrok");
        server.setUseEpoll(false);
        server.setBossThreads(1);
        server.setWorkerThreads(1);
        server.setWriteTimeout(20000);
        server.start();
        if (server.trafficLog()) {
            log.info("");
        }
        return server;
    }

    public static void main(String[] args) {
        SpringApplication.run(NgrokServerApplication.class, args);
    }
}
