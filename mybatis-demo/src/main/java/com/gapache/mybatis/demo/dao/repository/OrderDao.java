package com.gapache.mybatis.demo.dao.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gapache.mybatis.demo.dao.po.OrderPO;

/**
 * @author HuSen
 * create on 2020/5/13 11:43 上午
 */
public interface OrderDao {
    IPage<OrderPO> selectPage(IPage<OrderPO> page);
}
