package com.gapache.oms.store.location.sdk.feign;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.fallback.AreaFeignFallback;
import com.gapache.oms.store.location.sdk.model.vo.AreaVO;
import com.gapache.oms.store.location.sdk.model.vo.CityVO;
import com.gapache.oms.store.location.sdk.model.vo.ProvinceVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "oms-store-location-server", path = "/api/area", fallback = AreaFeignFallback.class)
public interface AreaFeign {

    @PostMapping("/refresh")
    JsonResult<Object> refresh();

    @GetMapping("/findAllProvince")
    JsonResult<List<ProvinceVO>> findAllProvince(@RequestParam(required = false) String name);

    @GetMapping("/findAllCity")
    JsonResult<List<CityVO>> findAllCity(@RequestParam String provinceCode, @RequestParam(required = false) String name);

    @GetMapping("/findAllArea")
    JsonResult<List<AreaVO>> findAllArea(@RequestParam String cityCode, @RequestParam(required = false) String name);
}
