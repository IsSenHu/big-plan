package com.gapache.cloud.alibaba.feign;

import com.gapache.commons.model.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author HuSen
 * @since 2020/6/15 5:15 下午
 */
@FeignClient(value = "nacos-payment-provider", path = "/api/payment", fallback = PaymentFeignFallback.class)
public interface PaymentFeign {

    @GetMapping("/getPort")
    JsonResult<String> getPort();
}
