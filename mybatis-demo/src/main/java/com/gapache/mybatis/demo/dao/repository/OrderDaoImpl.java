package com.gapache.mybatis.demo.dao.repository;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gapache.jpa.BaseJpaRepositoryBean;
import com.gapache.mybatis.demo.dao.mapper.OrderMapper;
import com.gapache.mybatis.demo.dao.po.OrderJPO;
import com.gapache.mybatis.demo.dao.po.OrderPO;

import javax.persistence.EntityManager;

/**
 * @author HuSen
 * create on 2020/5/13 11:43 上午
 */
public class OrderDaoImpl extends BaseJpaRepositoryBean<OrderJPO, Long> implements OrderDao {

    private final OrderMapper orderMapper;

    public OrderDaoImpl(EntityManager entityManager, OrderMapper orderMapper) {
        super(OrderJPO.class, entityManager);
        this.orderMapper = orderMapper;
    }

    @Override
    public IPage<OrderPO> selectPage(IPage<OrderPO> page) {
        return orderMapper.page(page);
    }
}
