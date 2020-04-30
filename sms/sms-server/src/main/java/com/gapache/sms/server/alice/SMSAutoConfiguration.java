package com.gapache.sms.server.alice;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.gapache.sms.server.alice.SMSAlice;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

/**
 * @author HuSen
 * create on 2020/1/11
 */
@Slf4j
@Data
@Configuration
public class SMSAutoConfiguration implements InitializingBean {

    @NacosValue(value = "${sms.regionId:aaa}")
    private String regionId;

    @NacosValue(value = "${sms.accessKeyId:bbb}")
    private String accessKeyId;

    @NacosValue(value = "${sms.secret:ccc}")
    private String secret;

    @NacosValue(value = "${sms.sysDomain:ddd}")
    private String sysDomain;

    @NacosValue(value = "${sms.sysVersion:eee}")
    private String sysVersion;

    @Override
    public void afterPropertiesSet() {
        Assert.hasText(regionId, "regionId is empty");
        Assert.hasText(accessKeyId, "accessKeyId is empty");
        Assert.hasText(secret, "secret is empty");
        Assert.hasText(sysDomain, "sysDomain is empty");
        Assert.hasText(sysVersion, "sysVersion is empty");
        log.info("Load sms config:{}", this);
    }

    @Bean
    public SMSAlice smsAlice() {
        return new SMSAlice(regionId, accessKeyId, secret, sysDomain, sysVersion);
    }
}
