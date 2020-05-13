package com.gapache.mybatis.demo.dao.po;

import com.gapache.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/5/13 9:45 上午
 */
@Setter
@Getter
@Entity
@Table(name = "tb_order")
@NamedEntityGraph(name = "OrderJPO.items", attributeNodes = {
        @NamedAttributeNode("items")
})
public class OrderJPO extends BaseEntity<Long> {

    @Column(name = "order_bn", nullable = false)
    private String orderBn;

    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderItemJPO> items;
}
