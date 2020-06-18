package com.gapache.cloud.seata.business.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author HuSen
 * @since 2020/6/17 4:11 下午
 */
@FeignClient(value = "seata-storage-service", path = "/api/storage", fallback = StorageFeignFallback.class)
public interface StorageFeign {

    /**
     * 扣除存储数量
     *
     * @param commodityCode 商品编码
     * @param count         数量
     */
    @PostMapping("/deduct")
    void deduct(@RequestParam String commodityCode, @RequestParam int count);
}
