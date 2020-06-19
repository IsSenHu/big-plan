package com.gapache.oms.order.base.model.vo.order;


import lombok.Getter;

/**
 * 订单状态
 *
 * @author HuSen
 * @since 2020/6/19 4:31 下午
 */
@Getter
public enum OrderStatus {
    // 待支付
    TO_BE_PAID(1),
    // 待发货
    TO_BE_DELIVERED(2),
    // 已发货
    SHIPPED(3),
    // 已收货
    RECEIVED(4),
    // 已取消
    CANCELLED(5);

    private Integer status;

    OrderStatus(Integer status) {
        this.status = status;
    }
}
