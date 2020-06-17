package com.gapache.cloud.seata.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author HuSen
 * @since 2020/6/17 4:11 下午
 */
@FeignClient(value = "seata-storage-service", path = "/api/storage")
public interface StorageFeign {

    /**
     * 扣除存储数量
     *
     * @param commodityCode 商品编码
     * @param count         数量
     */
    @PostMapping("/deduct")
    void deduct(String commodityCode, int count);
}
