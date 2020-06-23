package com.gapache.oms.store.location.server.service;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.AreaVO;
import com.gapache.oms.store.location.sdk.model.vo.CityVO;
import com.gapache.oms.store.location.sdk.model.vo.ProvinceVO;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/6/23 2:12 下午
 */
public interface AreaService {

    /**
     * 刷新省市区的数据
     */
    void refresh();

    /**
     * 根据名称模糊匹配省
     *
     * @param name 名称
     * @return 匹配到的省
     */
    JsonResult<List<ProvinceVO>> findAllProvince(String name);

    /**
     * 根据省编码和名称模糊匹配市
     *
     * @param provinceCode 省编码
     * @param name         名称
     * @return 匹配到的市
     */
    JsonResult<List<CityVO>> findAllCity(String provinceCode, String name);

    /**
     * 根据市编码和名称模糊匹配区
     *
     * @param cityCode 市编码
     * @param name     名称
     * @return 匹配到的区
     */
    JsonResult<List<AreaVO>> findAllArea(String cityCode, String name);
}
