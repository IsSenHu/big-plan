package com.gapache.oms.order.base.model.vo.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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
    private OrderStatus status;
    /**
     * 订单创建时间
     */
    private LocalDateTime createTime;
    /**
     * 最近一次修改时间
     */
    private LocalDateTime lastModifyTime;
    /**
     * 平台优惠
     */
    private BigDecimal pmtPlatform;
    /**
     * 订单优惠 店铺优惠
     */
    private BigDecimal pmtOrder;
    /**
     * 优惠详情
     */
    private List<PmtDetailVO> pmtDetails;
    /**
     * 商品花费金额
     */
    private BigDecimal costItem;
    /**
     * 订单总价（买家实际花费）
     */
    private BigDecimal curAmount;
    /**
     * 订单总金额（我方实际应收金额）
     */
    private BigDecimal totalAmount;
    /**
     * 已支付金额
     */
    private BigDecimal payed;
    /**
     * 支付状态
     */
    private PayStatus payStatus;
    /**
     * 收货地址/联系人信息
     */
    private ConsigneeVO consignee;
    /**
     * 发货信息
     */
    private ShipVO ship;
    /**
     * 订单条目
     */
    private List<OrderItemVO> items;
    /**
     * 指定门店编码
     */
    private String storeCode;
}
