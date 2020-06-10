package com.gapache.cloud.order.feign;

import com.gapache.cloud.sdk.PaymentVO;
import com.gapache.commons.model.JsonResult;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/6/10 10:47 上午
 */
@Component
public class PaymentFeignFallback implements PaymentFeign {

    @Override
    public JsonResult<PaymentVO> get(Long id) {
        return null;
    }

    @Override
    public Integer getPortTimeout() {
        return null;
    }
}
