package com.gapache.ngrok.cli;

import com.gapache.ngrok.cli.http.HttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author HuSen
 * create on 2020/4/11 4:22 下午
 */
@SpringBootApplication
public class NgrokCliApplication {

    @Bean
    public HttpClient client() {
        HttpClient httpClient = new HttpClient("127.0.0.1", 7777, "test", 100);
        httpClient.init();
        httpClient.start();
        return httpClient;
    }

    public static void main(String[] args) {
        SpringApplication.run(NgrokCliApplication.class, args);

    }
}
