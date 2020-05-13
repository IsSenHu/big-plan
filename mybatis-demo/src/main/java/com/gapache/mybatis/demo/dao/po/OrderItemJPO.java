package com.gapache.mybatis.demo.dao.po;

import com.gapache.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author HuSen
 * create on 2020/5/13 9:45 上午
 */
@Setter
@Getter
@Entity
@Table(name = "tb_order_item")
public class OrderItemJPO extends BaseEntity<Long> {
    @Column(name = "price", nullable = false)
    private Double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_bn")
    private OrderJPO order;
}
