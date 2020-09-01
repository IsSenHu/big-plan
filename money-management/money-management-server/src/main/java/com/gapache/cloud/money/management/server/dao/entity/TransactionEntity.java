package com.gapache.cloud.money.management.server.dao.entity;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * @since 2020/8/11 10:08 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_transaction")
public class TransactionEntity extends BaseEntity<Long> {
    /**
     * 关联用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    /**
     * 交易号
     */
    @Column(name = "transaction_id")
    private String transactionId;
    /**
     * 商家订单号
     */
    @Column(name = "order_bn")
    private String orderBn;
    /**
     * 交易创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;
    /**
     * 付款时间
     */
    @Column(name = "pay_time")
    private LocalDateTime payTime;
    /**
     * 最近修改时间
     */
    @Column(name = "last_modify_time")
    private LocalDateTime lastModifyTime;
    /**
     * 交易来源地
     */
    @Column(name = "source")
    private String source;
    /**
     * 类型
     */
    @Column(name = "type")
    private String type;
    /**
     * 交易对方
     */
    @Column(name = "target")
    private String target;
    /**
     * 商品名称
     */
    @Column(name = "goods_name")
    private String goodsName;
    /**
     * 金额（元）
     */
    @Column(name = "amount")
    private BigDecimal amount;
    /**
     * 收或支
     */
    @Column(name = "income_or_expenditure")
    private String incomeOrExpenditure;
    /**
     * 交易状态
     */
    @Column(name = "status")
    private String status;
    /**
     * 服务费（元）
     */
    @Column(name = "service_cost")
    private BigDecimal serviceCost;
    /**
     * 成功退款（元）
     */
    @Column(name = "refund")
    private BigDecimal refund;
    /**
     * 备注
     */
    @Column(name = "mark")
    private String mark;
    /**
     * 资金状态
     */
    @Column(name = "funds_status")
    private String fundsStatus;
}
