package com.gapache.oms.store.location.server.dao.repository;

import com.gapache.jpa.BaseJpaRepository;
import com.gapache.oms.store.location.server.dao.po.StorePO;

/**
 * @author HuSen
 * @since 2020/6/24 11:30 上午
 */
public interface StoreRepository extends BaseJpaRepository<StorePO, Integer> {

    /**
     * 通过门店编码查询门店
     *
     * @param code 门店编码
     * @return 门店
     */
    StorePO findByCode(String code);
}
