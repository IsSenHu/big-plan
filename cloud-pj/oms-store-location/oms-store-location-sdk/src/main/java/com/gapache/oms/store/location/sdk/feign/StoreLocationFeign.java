package com.gapache.oms.store.location.sdk.feign;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.fallback.StoreLocationFeignFallback;
import com.gapache.oms.store.location.sdk.model.vo.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Husen
 * @since 2020/06/23 23:59:59
 */
@FeignClient(value = "oms-store-location-server", path = "/api", fallback = StoreLocationFeignFallback.class)
public interface StoreLocationFeign {

    /**
     * 刷新省市区信息
     *
     * @return 刷新结果
     */
    @PostMapping("/area/refresh")
    JsonResult<Object> refresh();

    /**
     * 根据名称模糊匹配省
     *
     * @param name 名称
     * @return 匹配到的省
     */
    @GetMapping("/area/findAllProvince")
    JsonResult<List<ProvinceVO>> findAllProvince(@RequestParam(required = false) String name);

    /**
     * 根据名称模糊匹配市
     *
     * @param provinceCode 省编码
     * @param name 市名称
     * @return 匹配到的市
     */
    @GetMapping("/area/findAllCity")
    JsonResult<List<CityVO>> findAllCity(@RequestParam String provinceCode, @RequestParam(required = false) String name);

    /**
     * 根据名称模糊匹配区
     *
     * @param cityCode 市编码
     * @param name 区名称
     * @return 匹配到的区
     */
    @GetMapping("/area/findAllArea")
    JsonResult<List<AreaVO>> findAllArea(@RequestParam String cityCode, @RequestParam(required = false) String name);

    /**
     * 根据地址反解析出经纬度
     *
     * @param city    所在城市
     * @param address 详细地址
     * @return 解析结果
     */
    @GetMapping("/geo/geocode/geo")
    JsonResult<GeoCodeGeoResponseVO> geocodeGeo(@RequestParam(required = false) String city, @RequestParam String address);

    /**
     * 根据提供的地址查询距离最近或者最符合发货条件的门店
     *
     * @param city    市
     * @param address 详细地址
     * @return 匹配到的门店
     */
    @GetMapping("/store/findClosestDistanceByAddress")
    JsonResult<StoreVO> findClosestDistanceByAddress(@RequestParam(required = false) String city, @RequestParam String address);
}
