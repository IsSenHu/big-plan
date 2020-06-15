package com.gapache.cloud.alibaba.feign;

import com.gapache.commons.model.JsonResult;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/6/15 5:18 下午
 */
@Component
public class PaymentFeignFallback implements PaymentFeign {

    @Override
    public JsonResult<String> getPort() {
        return JsonResult.of("PaymentFeignFallback");
    }
}
