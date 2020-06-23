package com.gapache.oms.store.location.sdk.fallback;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.order.base.model.error.ServerCommonError;
import com.gapache.oms.store.location.sdk.feign.AreaFeign;
import com.gapache.oms.store.location.sdk.model.vo.AreaVO;
import com.gapache.oms.store.location.sdk.model.vo.CityVO;
import com.gapache.oms.store.location.sdk.model.vo.ProvinceVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class AreaFeignFallback implements AreaFeign {

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
}
