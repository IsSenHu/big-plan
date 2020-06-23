package com.gapache.oms.store.location.server.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gapache.oms.store.location.sdk.model.vo.AreaVO;
import com.gapache.oms.store.location.sdk.model.vo.CityVO;
import com.gapache.oms.store.location.sdk.model.vo.ProvinceVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author HuSen
 * @since 2020/6/23 11:35 上午
 */
@Slf4j
public class AreaUtils {

    private static final String PROVINCE_CODE = "86";

    /**
     * 生成省市区数据
     *
     * @param path 省市区classpath下的路径
     * @return 省市区数据
     */
    public static List<ProvinceVO> checkAll(String path) {
        ClassPathResource resource = new ClassPathResource(path);
        try {
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String jsonContext = new String(bytes, StandardCharsets.UTF_8);
            if (StringUtils.isBlank(jsonContext)) {
                return null;
            }
            List<ProvinceVO> result = new ArrayList<>();
            JSONObject context = JSON.parseObject(jsonContext);
            JSONObject provinces = context.getJSONObject(PROVINCE_CODE);
            Set<String> provinceCodes = provinces.keySet();
            for (String provinceCode : provinceCodes) {
                ProvinceVO province = new ProvinceVO();
                province.setCode(provinceCode);
                province.setName(provinces.getString(provinceCode));
                result.add(province);

                JSONObject cities = context.getJSONObject(provinceCode);
                if (Objects.isNull(cities)) {
                    continue;
                }
                Set<String> cityCodes = cities.keySet();
                List<CityVO> provinceCities = new ArrayList<>(cityCodes.size());

                for (String cityCode : cityCodes) {
                    CityVO city = new CityVO();
                    city.setCode(cityCode);
                    city.setProvinceCode(provinceCode);
                    city.setName(cities.getString(cityCode));
                    provinceCities.add(city);

                    JSONObject areas = context.getJSONObject(cityCode);
                    if (Objects.isNull(areas)) {
                        continue;
                    }
                    Set<String> areaCodes = areas.keySet();
                    List<AreaVO> cityAreas = new ArrayList<>(areaCodes.size());

                    for (String areaCode : areaCodes) {
                        AreaVO area = new AreaVO();
                        area.setCode(areaCode);
                        area.setCityCode(cityCode);
                        area.setName(areas.getString(areaCode));
                        cityAreas.add(area);
                    }
                    city.setAreas(cityAreas);
                }
                province.setCities(provinceCities);
            }
            return result;
        } catch (Exception e) {
            log.error("[{}] luaScript is load fail:", path, e);
            return null;
        }
    }
}
