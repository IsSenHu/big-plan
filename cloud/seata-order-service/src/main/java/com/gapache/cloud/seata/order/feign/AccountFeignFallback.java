package com.gapache.cloud.seata.order.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/6/18 3:10 下午
 */
@Slf4j
@Component
public class AccountFeignFallback implements AccountFeign {

    @Override
    public void debit(String userId, int money) {
        if (log.isInfoEnabled()) {
            log.info("debit userId:{}, money:{} fallback.", userId, money);
        }
    }
}
