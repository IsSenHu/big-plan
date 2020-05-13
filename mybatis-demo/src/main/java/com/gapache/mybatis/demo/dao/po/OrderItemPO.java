package com.gapache.mybatis.demo.dao.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author HuSen
 * create on 2020/5/13 9:45 上午
 */
@Data
@TableName("tb_order_item")
public class OrderItemPO {
    private Long id;
    private String orderBn;
    private Double price;
}
