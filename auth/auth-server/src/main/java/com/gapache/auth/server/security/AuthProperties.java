package com.gapache.auth.server.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/7 12:44 下午
 */
@Data
@ConfigurationProperties("gapache.auth")
public class AuthProperties implements Serializable {
    private static final long serialVersionUID = -4038373865182554105L;

    private String header;
    private String secret;
    private Integer expire;
}
