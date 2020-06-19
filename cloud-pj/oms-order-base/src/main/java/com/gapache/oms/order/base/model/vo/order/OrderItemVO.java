package com.gapache.oms.order.base.model.vo.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 单个订单项
 *
 * @author HuSen
 * @since 2020/6/19 10:32 上午
 */
@Data
public class OrderItemVO implements Serializable {
    private static final long serialVersionUID = -1475802634551492979L;
    /**
     * 商品编码
     */
    private String bn;
    /**
     * 商品名称
     */
    private String name;
    /**
     * 商品单价
     */
    private BigDecimal price;
    /**
     * 商品数量
     */
    private Integer count;
    /**
     * 商品总价
     */
    private BigDecimal amount;
}
