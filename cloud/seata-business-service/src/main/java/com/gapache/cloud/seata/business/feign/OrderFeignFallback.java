package com.gapache.cloud.seata.business.feign;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.cloud.seata.business.error.OrderError;
import com.gapache.commons.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/6/18 2:58 下午
 */
@Slf4j
@Component
public class OrderFeignFallback implements OrderFeign {

    @Override
    public JsonResult<OrderVO> create(String userId, String commodityCode, int orderCount) {
        if (log.isInfoEnabled()) {
            log.info("create order userId:{}, commodityCode:{}, orderCount:{} fallback,", userId, commodityCode, orderCount);
        }
        return JsonResult.of(OrderError.CREATE_FAIL);
    }
}
