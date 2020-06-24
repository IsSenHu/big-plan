package com.gapache.oms.store.location.server.controller;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.GeoCodeGeoResponseVO;
import com.gapache.oms.store.location.server.service.GeoService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/24 3:08 下午
 */
@RestController
@RequestMapping("/api/geo")
public class GeoController {

    @Resource
    private GeoService geoService;

    @GetMapping("/geocode/geo")
    public JsonResult<GeoCodeGeoResponseVO> geocodeGeo(@RequestParam(required = false) String city, String address) {
        return geoService.geocodeGeo(city, address);
    }
}
