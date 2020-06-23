package com.gapache.oms.store.location.server.dao.repository;

import com.gapache.jpa.BaseJpaRepository;
import com.gapache.oms.store.location.server.dao.po.CityPO;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/6/23 2:10 下午
 */
public interface CityRepository extends BaseJpaRepository<CityPO, Integer> {

    /**
     * 根据省编码以及名称进行模糊匹配市
     *
     * @param provinceCode 省编码
     * @param name         名称
     * @return 匹配到的市
     */
    List<CityPO> findAllByProvinceCodeAndNameLike(String provinceCode, String name);

    /**
     * 根据省编码查询市
     *
     * @param provinceCode 省编码
     * @return 匹配到的市
     */
    List<CityPO> findAllByProvinceCode(String provinceCode);
}
