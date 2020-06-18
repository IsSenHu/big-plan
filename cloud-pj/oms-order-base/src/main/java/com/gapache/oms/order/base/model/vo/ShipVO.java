package com.gapache.oms.order.base.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 发货信息
 *
 * @author HuSen
 * @since 2020/6/18 6:23 下午
 */
@Data
public class ShipVO implements Serializable {
    private static final long serialVersionUID = -8010721938271243539L;
    /**
     * 发货快递名称
     */
    private String name;
    /**
     * 是否货到付款
     */
    private Boolean isCod;
    /**
     * 邮费
     */
    private BigDecimal cost;
    /**
     * 订单号
     */
    private String orderBn;
}
