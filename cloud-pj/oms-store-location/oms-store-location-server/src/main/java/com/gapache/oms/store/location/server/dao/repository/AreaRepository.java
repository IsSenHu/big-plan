package com.gapache.oms.store.location.server.dao.repository;

import com.gapache.jpa.BaseJpaRepository;
import com.gapache.oms.store.location.server.dao.po.AreaPO;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/6/23 2:11 下午
 */
public interface AreaRepository extends BaseJpaRepository<AreaPO, Integer> {

    /**
     * 根据市编码和名称模糊匹配区
     *
     * @param cityCode 市编码
     * @param name     名称
     * @return 匹配到的区
     */
    List<AreaPO> findAllByCityCodeAndNameLike(String cityCode, String name);

    /**
     * 根据市编码查询区
     *
     * @param cityCode 市编码
     * @return 匹配到的区
     */
    List<AreaPO> findAllByCityCode(String cityCode);
}
