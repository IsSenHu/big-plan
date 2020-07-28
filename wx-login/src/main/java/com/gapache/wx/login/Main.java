package com.gapache.wx.login;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * @since 2020/7/28 10:13 上午
 */
@SpringBootApplication
@RestController
public class Main {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @PostMapping("/login")
    @CrossOrigin("*")
    public Map login(String code) {
        Map<String, Object> params = new HashMap<>(1);
        params.put("appid", "wx69990b1463bed1e8");
        params.put("secret", "17e609cf99f25862109bd541dedc49a9");
        params.put("js_code", code);
        params.put("grant_type", "authorization_code");
        return restTemplate().getForObject("https://api.weixin.qq.com/sns/jscode2session", Map.class, params);
    }
}
