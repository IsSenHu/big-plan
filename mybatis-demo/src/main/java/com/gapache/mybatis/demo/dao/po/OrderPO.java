package com.gapache.mybatis.demo.dao.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/5/13 9:45 上午
 */
@Data
@TableName("tb_order")
public class OrderPO {
    private Long id;
    private String orderBn;

    @TableField(exist = false)
    private List<OrderItemPO> items;
}
