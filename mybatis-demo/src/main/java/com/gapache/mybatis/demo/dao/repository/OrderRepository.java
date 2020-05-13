package com.gapache.mybatis.demo.dao.repository;

import com.gapache.jpa.BaseJpaRepository;
import com.gapache.mybatis.demo.dao.po.OrderJPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;

/**
 * @author HuSen
 * create on 2020/5/13 11:22 上午
 */
public interface OrderRepository extends BaseJpaRepository<OrderJPO, Long>, OrderDao {

    @Override
    @EntityGraph("OrderJPO.items")
    Page<OrderJPO> findAll(Pageable pageable);
}
