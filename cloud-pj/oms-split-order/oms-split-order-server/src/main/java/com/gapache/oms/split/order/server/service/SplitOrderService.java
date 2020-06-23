package com.gapache.oms.split.order.server.service;

import com.gapache.oms.order.base.model.vo.order.OrderBaseVO;

/**
 * 分单服务
 *
 * @author HuSen
 * @since 2020/6/22 4:50 下午
 */
public interface SplitOrderService {

    /**
     * 自动分单
     *
     * @param order 订单
     */
    void autoSplitOrder(OrderBaseVO order);
}
