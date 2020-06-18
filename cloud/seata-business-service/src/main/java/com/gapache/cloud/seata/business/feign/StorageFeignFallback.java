package com.gapache.cloud.seata.business.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/6/18 3:07 下午
 */
@Slf4j
@Component
public class StorageFeignFallback implements StorageFeign {

    @Override
    public void deduct(String commodityCode, int count) {
        if (log.isInfoEnabled()) {
            log.info("deduct:{}, count:{} fallback.", commodityCode, count);
        }
    }
}
