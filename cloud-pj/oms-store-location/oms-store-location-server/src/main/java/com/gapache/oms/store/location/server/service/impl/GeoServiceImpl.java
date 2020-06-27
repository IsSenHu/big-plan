package com.gapache.oms.store.location.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.error.StoreLocationError;
import com.gapache.oms.store.location.sdk.model.vo.GeoCodeGeoResponseVO;
import com.gapache.oms.store.location.server.sdk.IMapSdk;
import com.gapache.oms.store.location.server.service.GeoService;
import org.springframework.data.util.Pair;
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
        JSONObject result = iMapSdk.geocodeGeo(city, address);
        int status = result.getIntValue("status");
        int count = result.getIntValue("count");
        if (status != 1 && count < 1) {
            return JsonResult.of(StoreLocationError.GEOCODE_GEO_PARSE_FAIL);
        }
        JSONArray geoCodes = result.getJSONArray("geocodes");
        if (geoCodes == null || geoCodes.size() < 1) {
            return JsonResult.of(StoreLocationError.GEOCODE_GEO_PARSE_FAIL);
        }

        JSONObject geoCode = geoCodes.getJSONObject(0);
        GeoCodeGeoResponseVO response = new GeoCodeGeoResponseVO();
        response.setFormattedAddress(geoCode.getString("formatted_address"));
        response.setCountry(geoCode.getString("country"));
        response.setProvince(geoCode.getString("province"));
        response.setCityCode(geoCode.getString("citycode"));
        response.setCity(geoCode.getString("city"));
        response.setDistrict(geoCode.getString("district"));
        response.setAdCode(geoCode.getString("adcode"));
        response.setLevel(geoCode.getString("level"));

        String location = geoCode.getString("location");
        String[] split = location.split(",");
        // 纬度
        response.setLongitude(Double.valueOf(split[0]));
        // 经度
        response.setLatitude(Double.valueOf(split[1]));
        return JsonResult.of(response);
    }

    @Override
    public Pair<Double, Double> geocodeGeoOnlyLatLon(String city, String address) {
        JSONObject result = iMapSdk.geocodeGeo(city, address);
        int status = result.getIntValue("status");
        int count = result.getIntValue("count");
        if (status != 1 && count < 1) {
            return null;
        }
        JSONArray geoCodes = result.getJSONArray("geocodes");
        JSONObject geoCode = geoCodes.getJSONObject(0);
        String location = geoCode.getString("location");
        String[] split = location.split(",");
        return Pair.of(Double.valueOf(split[0]), Double.valueOf(split[1]));
    }
}
