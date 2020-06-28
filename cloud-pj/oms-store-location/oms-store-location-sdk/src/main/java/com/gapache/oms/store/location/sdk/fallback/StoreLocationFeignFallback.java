package com.gapache.oms.store.location.sdk.fallback;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.order.base.model.error.ServerCommonError;
import com.gapache.oms.store.location.sdk.feign.StoreLocationFeign;
import com.gapache.oms.store.location.sdk.model.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Husen
 * @since 2020/06/23 23:59:59
 */
@Slf4j
@Component
public class StoreLocationFeignFallback implements StoreLocationFeign {

    @Override
    public JsonResult<Object> refresh() {
        log.warn("刷新省市区数据服务进入降级!!!");
        return JsonResult.of(ServerCommonError.FALLBACK);
    }

    @Override
    public JsonResult<List<ProvinceVO>> findAllProvince(String name) {
        log.warn("查询省服务进入服务降级!!!");
        return JsonResult.of(ServerCommonError.FALLBACK);
    }

    @Override
    public JsonResult<List<CityVO>> findAllCity(String provinceCode, String name) {
        log.warn("查询市服务进入服务降级!!!");
        return JsonResult.of(ServerCommonError.FALLBACK);
    }

    @Override
    public JsonResult<List<AreaVO>> findAllArea(String cityCode, String name) {
        log.warn("查询区服务进入服务降级!!!");
        return JsonResult.of(ServerCommonError.FALLBACK);
    }

    @Override
    public JsonResult<GeoCodeGeoResponseVO> geocodeGeo(String city, String address) {
        log.warn("查询区服务进入服务降级!!!");
        return JsonResult.of(ServerCommonError.FALLBACK);
    }

    @Override
    public JsonResult<StoreVO> findClosestDistanceByAddress(String city, String address) {
        log.warn("查询区服务进入服务降级!!!");
        return JsonResult.of(ServerCommonError.FALLBACK);
    }

    @Override
    public JsonResult<StoreVO> findStore(String code) {
        log.warn("查询区服务进入服务降级!!!");
        return JsonResult.of(ServerCommonError.FALLBACK);
    }
}
