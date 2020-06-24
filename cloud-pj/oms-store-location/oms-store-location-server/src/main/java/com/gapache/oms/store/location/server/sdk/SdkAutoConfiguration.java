package com.gapache.oms.store.location.server.sdk;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author HuSen
 * @since 2020/6/24 3:32 下午
 */
@Configuration
public class SdkAutoConfiguration {

    @Bean
    public IMapSdk iMapSdk() {
        IMapSdk sdk = new IMapSdk("imap.json");
        sdk.init();
        return sdk;
    }
}
