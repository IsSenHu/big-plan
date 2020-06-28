package com.gapache.oms.store.location.server.dao.repository;

import com.gapache.jpa.BaseJpaRepository;
import com.gapache.oms.store.location.sdk.model.enums.StoreType;
import com.gapache.oms.store.location.server.dao.po.StorePO;

import java.util.List;

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

    /**
     * 根据门店所在城市和门店类型查找门店
     *
     * @param city      所在城市
     * @param storeType 门店类型
     * @return 门店
     */
    StorePO findByCityAndStoreType(String city, StoreType storeType);

    /**
     * 根据门店所在省和门店类型查找门店
     *
     * @param province  所在省
     * @param storeType 门店类型
     * @return 门店
     */
    StorePO findByProvinceAndStoreType(String province, StoreType storeType);

    /**
     * 根据门店类型查找门店
     *
     * @param storeType 门店类型
     * @return 门店
     */
    List<StorePO> findAllByStoreType(StoreType storeType);
}
