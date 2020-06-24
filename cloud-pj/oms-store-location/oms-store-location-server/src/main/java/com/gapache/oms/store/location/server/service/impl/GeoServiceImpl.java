package com.gapache.oms.store.location.server.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.GeoCodeGeoResponseVO;
import com.gapache.oms.store.location.server.sdk.IMapSdk;
import com.gapache.oms.store.location.server.service.GeoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/24 3:10 下午
 */
@Service
public class GeoServiceImpl implements GeoService {

    @Resource
    private IMapSdk iMapSdk;

    @Override
    public JsonResult<GeoCodeGeoResponseVO> geocodeGeo(String city, String address) {
        return null;
    }
}
