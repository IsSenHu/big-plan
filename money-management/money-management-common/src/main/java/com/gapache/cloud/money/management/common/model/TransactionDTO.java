package com.gapache.cloud.money.management.common.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * @since 2020/8/13 5:08 下午
 */
@Data
public class TransactionDTO implements Serializable {
    private static final long serialVersionUID = 1919719787238089371L;
    /**
     * ID
     */
    private Long id;
    /**
     * 关联用户ID
     */
    private Long userId;
    /**
     * 交易号
     */
    private String transactionId;
    /**
     * 商家订单号
     */
    private String orderBn;
    /**
     * 交易创建时间
     */
    private LocalDateTime createTime;
    /**
     * 付款时间
     */
    private LocalDateTime payTime;
    /**
     * 最近修改时间
     */
    private LocalDateTime lastModifyTime;
    /**
     * 交易来源地
     */
    private String source;
    /**
     * 类型
     */
    private String type;
    /**
     * 交易对方
     */
    private String target;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 金额（元）
     */
    private BigDecimal amount;
    /**
     * 收或支
     */
    private String incomeOrExpenditure;
    /**
     * 交易状态
     */
    private String status;
    /**
     * 服务费（元）
     */
    private BigDecimal serviceCost;
    /**
     * 成功退款（元）
     */
    private BigDecimal refund;
    /**
     * 备注
     */
    private String mark;
    /**
     * 资金状态
     */
    private String fundsStatus;
}
