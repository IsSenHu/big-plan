package com.gapache.user.service;

import com.gapache.uid.UidGenerator;
import com.gapache.uid.annotation.EnableUidGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * create on 2020/1/8 18:01
 */
@EnableUidGenerator
@SpringBootApplication
@RestController
public class ServiceApplication {

    public ServiceApplication(UidGenerator uidGenerator) {
        this.uidGenerator = uidGenerator;
    }

    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    private final UidGenerator uidGenerator;

    @GetMapping
    public long test() {
        return uidGenerator.getUID();
    }
}
