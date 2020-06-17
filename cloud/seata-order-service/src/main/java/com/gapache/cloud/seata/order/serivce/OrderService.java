package com.gapache.cloud.seata.order.serivce;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.commons.model.JsonResult;

/**
 * @author HuSen
 * @since 2020/6/17 4:29 下午
 */
public interface OrderService {

    /**
     * 创建订单
     *
     * @param userId        用户ID
     * @param commodityCode 商品编码
     * @param orderCount    订单总金额
     * @return 创建结果
     */
    JsonResult<OrderVO> create(String userId, String commodityCode, int orderCount);
}
