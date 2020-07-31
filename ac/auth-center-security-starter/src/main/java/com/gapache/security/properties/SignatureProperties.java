package com.gapache.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author HuSen
 * @since 2020/7/31 1:10 下午
 */
@Setter
@Getter
@ConfigurationProperties(prefix = "com.gapache.security")
public class SignatureProperties {
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 超时时间
     */
    private Long timeout = 36000000L;
}
