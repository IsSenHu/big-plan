package com.gapache.jpa;

import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/29 3:47 下午
 */
public class JpaAutoConfiguration<OP_MAN extends Serializable> {

    @Bean
    public AuditorAware<OP_MAN> auditorAware() {
        return new EntityAuditorAware<>();
    }
}
