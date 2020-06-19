package com.gapache.oms.order.base.model.vo.order;

import lombok.Getter;

/**
 * 支付状态
 *
 * @author HuSen
 * @since 2020/6/19 4:41 下午
 */
@Getter
public enum PayStatus {
    // 未支付
    UNPAID(0),
    // 已支付
    PAID(1);

    private Integer status;

    PayStatus(Integer status) {
        this.status = status;
    }
}
