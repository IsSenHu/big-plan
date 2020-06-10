package com.gapache.cloud.order.feign;

import com.gapache.cloud.sdk.PaymentVO;
import com.gapache.commons.model.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author HuSen
 * @since 2020/6/5 6:12 下午
 */
@FeignClient(value = "CLOUD-PAYMENT-SERVICE", path = "/api/payment", fallback = PaymentFeignFallback.class)
public interface PaymentFeign {

    @GetMapping("/{id}")
    JsonResult<PaymentVO> get(@PathVariable("id") Long id);

    @GetMapping("/getPortTimeout")
    Integer getPortTimeout();
}
