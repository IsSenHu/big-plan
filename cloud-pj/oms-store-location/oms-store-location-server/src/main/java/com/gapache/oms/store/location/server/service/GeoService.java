package com.gapache.oms.store.location.server.service;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.GeoCodeGeoResponseVO;
import org.springframework.data.util.Pair;

/**
 * @author HuSen
 * @since 2020/6/24 3:10 下午
 */
public interface GeoService {

    /**
     * 根据地址反解析出经纬度
     *
     * @param city    所在城市
     * @param address 详细地址
     * @return 解析结果
     */
    JsonResult<GeoCodeGeoResponseVO> geocodeGeo(String city, String address);

    /**
     * 根据地址反解析出经纬度
     *
     * @param city    所在城市
     * @param address 详细地址
     * @return 经纬度 左边经度，右边纬度
     */
    Pair<Double, Double> geocodeGeoOnlyLatLon(String city, String address);
}