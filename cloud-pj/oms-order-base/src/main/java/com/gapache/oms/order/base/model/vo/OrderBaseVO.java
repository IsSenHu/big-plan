package com.gapache.oms.order.base.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单中心基础订单数据模型
 *
 * @author HuSen
 * @since 2020/6/18 5:29 下午
 */
@Data
public class OrderBaseVO implements Serializable {
    private static final long serialVersionUID = 268656234948650796L;
    /**
     * 订单编号
     */
    private String orderBn;
    /**
     * 订单状态
     */
    private String status;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最近一次修改时间
     */
    private LocalDateTime lastModifyTime;
    /**
     * 收货地址/联系人信息
     */
    private ConsigneeVO consignee;
    /**
     * 发货信息
     */
    private ShipVO ship;
    // TODO 商品明细 支付信息 订单优惠信息
}
