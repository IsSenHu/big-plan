package com.gapache.cloud.seata.storage.dao.repository;

import com.gapache.cloud.seata.storage.dao.po.StoragePO;
import com.gapache.jpa.BaseJpaRepository;

/**
 * @author HuSen
 * @since 2020/6/17 5:22 下午
 */
public interface StorageRepository extends BaseJpaRepository<StoragePO, Long> {

    /**
     * 查询商品库存信息
     *
     * @param commodityCode 商品编码
     * @return 商品库存信息
     */
    StoragePO findByCommodityCode(String commodityCode);
}
