package com.gapache.cloud.seata.order.dao.repository;

import com.gapache.cloud.seata.order.dao.po.OrderPO;
import com.gapache.jpa.BaseJpaRepository;

/**
 * @author HuSen
 * @since 2020/6/17 4:35 下午
 */
public interface OrderRepository extends BaseJpaRepository<OrderPO, Long> {

}
