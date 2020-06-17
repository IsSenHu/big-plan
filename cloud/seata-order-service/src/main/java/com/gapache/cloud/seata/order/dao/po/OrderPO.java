package com.gapache.cloud.seata.order.dao.po;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/6/17 4:35 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_order")
public class OrderPO extends BaseEntity<Long> {

    @Column(name = "user_id")
    private String userId;

    @Column(name = "order_bn", nullable = false, unique = true)
    private String orderBn;

    @Column(name = "commodity_code")
    private String commodityCode;

    @Column(name = "count")
    private Integer count;

    @Column(name = "money")
    private Integer money;
}
