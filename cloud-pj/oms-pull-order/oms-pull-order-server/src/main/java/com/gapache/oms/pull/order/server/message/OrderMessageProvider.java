package com.gapache.oms.pull.order.server.message;

import com.gapache.oms.order.base.model.vo.order.OrderBaseVO;

/**
 * @author HuSen
 * @since 2020/6/19 3:53 下午
 */
public interface OrderMessageProvider {

    /**
     * 发送订单信息
     *
     * @param orderBase 订单信息
     */
    void send(OrderBaseVO orderBase);
}
