package com.gapache.cloud.seata.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author HuSen
 * @since 2020/6/17 4:47 下午
 */
@FeignClient(value = "seata-account-service", path = "/api/account", fallback = AccountFeignFallback.class)
public interface AccountFeign {

    /**
     * 从用户账户中借出
     *
     * @param userId 用户ID
     * @param money  金额
     */
    @PostMapping("/debit")
    void debit(@RequestParam String userId, @RequestParam int money);
}
