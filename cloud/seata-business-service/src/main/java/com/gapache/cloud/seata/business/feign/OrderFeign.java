package com.gapache.cloud.seata.business.feign;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.commons.model.JsonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author HuSen
 * @since 2020/6/17 4:07 下午
 */
@FeignClient(value = "seata-order-service", path = "/api/order", fallback = OrderFeignFallback.class)
public interface OrderFeign {

    /**
     * 创建订单
     *
     * @param userId        用户ID
     * @param commodityCode 商品编码
     * @param orderCount    下单数量
     * @return 创建结果
     */
    @PostMapping("/create")
    JsonResult<OrderVO> create(@RequestParam String userId, @RequestParam String commodityCode, @RequestParam int orderCount);
}
