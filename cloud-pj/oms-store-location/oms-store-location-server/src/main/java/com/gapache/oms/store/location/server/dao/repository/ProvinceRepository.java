package com.gapache.oms.store.location.server.dao.repository;

import com.gapache.jpa.BaseJpaRepository;
import com.gapache.oms.store.location.server.dao.po.ProvincePO;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/6/23 2:10 下午
 */
public interface ProvinceRepository extends BaseJpaRepository<ProvincePO, Integer> {

    /**
     * 根据名称模糊匹配省
     *
     * @param name 名称
     * @return 匹配到的省
     */
    List<ProvincePO> findAllByNameLike(String name);
}
