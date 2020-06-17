package com.gapache.cloud.seata.business.service;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.commons.model.JsonResult;

/**
 * @author HuSen
 * @since 2020/6/17 4:12 下午
 */
public interface BusinessService {

    /**
     * 采购
     *
     * @param userId        用户ID
     * @param commodityCode 商品编码
     * @param orderCount    下单数量
     * @return              采购订单信息
     */
    JsonResult<OrderVO> purchase(String userId, String commodityCode, int orderCount);
}
