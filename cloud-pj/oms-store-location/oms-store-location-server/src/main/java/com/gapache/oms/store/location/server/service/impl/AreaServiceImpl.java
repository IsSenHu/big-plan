package com.gapache.oms.store.location.server.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.AreaVO;
import com.gapache.oms.store.location.sdk.model.vo.CityVO;
import com.gapache.oms.store.location.sdk.model.vo.ProvinceVO;
import com.gapache.oms.store.location.server.dao.po.AreaPO;
import com.gapache.oms.store.location.server.dao.po.CityPO;
import com.gapache.oms.store.location.server.dao.po.ProvincePO;
import com.gapache.oms.store.location.server.dao.repository.AreaRepository;
import com.gapache.oms.store.location.server.dao.repository.CityRepository;
import com.gapache.oms.store.location.server.dao.repository.ProvinceRepository;
import com.gapache.oms.store.location.server.service.AreaService;
import com.gapache.oms.store.location.server.utils.AreaUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/6/23 2:12 下午
 */
@Service
public class AreaServiceImpl implements AreaService {

    private static final String DATA_PATH = "area.json";

    @Resource
    private ProvinceRepository provinceRepository;

    @Resource
    private CityRepository cityRepository;

    @Resource
    private AreaRepository areaRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void refresh() {
        List<ProvinceVO> provinces = AreaUtils.checkAll(DATA_PATH);
        if (CollectionUtils.isEmpty(provinces)) {
            return;
        }
        provinceRepository.deleteAll();
        cityRepository.deleteAll();
        areaRepository.deleteAll();

        provinces.forEach(p -> {
            ProvincePO province = new ProvincePO();
            province.setCode(p.getCode());
            province.setName(p.getName());
            provinceRepository.save(province);

            List<CityVO> cities = p.getCities();
            if (CollectionUtils.isEmpty(cities)) {
                return;
            }
            cities.forEach(c -> {
                CityPO city = new CityPO();
                city.setCode(c.getCode());
                city.setName(c.getName());
                city.setProvinceCode(c.getProvinceCode());
                cityRepository.save(city);

                List<AreaVO> areas = c.getAreas();
                if (CollectionUtils.isEmpty(areas)) {
                    return;
                }
                areas.forEach(a -> {
                    AreaPO area = new AreaPO();
                    area.setCode(a.getCode());
                    area.setName(a.getName());
                    area.setCityCode(a.getCityCode());
                    areaRepository.save(area);
                });
            });
        });
    }

    @Override
    public JsonResult<List<ProvinceVO>> findAllProvince(String name) {
        List<ProvincePO> allByNameLike = StringUtils.isNotBlank(name) ?
                provinceRepository.findAllByNameLike("%".concat(name).concat("%")) : provinceRepository.findAll();
        List<ProvinceVO> result = allByNameLike.stream().map(po -> {
            ProvinceVO vo = new ProvinceVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList());
        return JsonResult.of(result);
    }

    @Override
    public JsonResult<List<CityVO>> findAllCity(String provinceCode, String name) {
        List<CityPO> allByNameLike = StringUtils.isNotBlank(name) ?
                cityRepository.findAllByProvinceCodeAndNameLike(provinceCode, "%".concat(name).concat("%")) : cityRepository.findAllByProvinceCode(provinceCode);
        List<CityVO> result = allByNameLike.stream().map(po -> {
            CityVO vo = new CityVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList());
        return JsonResult.of(result);
    }

    @Override
    public JsonResult<List<AreaVO>> findAllArea(String cityCode, String name) {
        List<AreaPO> allByNameLike = StringUtils.isNotBlank(name) ?
                areaRepository.findAllByCityCodeAndNameLike(cityCode, "%".concat(name).concat("%")) : areaRepository.findAllByCityCode(cityCode);
        List<AreaVO> result = allByNameLike.stream().map(po -> {
            AreaVO vo = new AreaVO();
            BeanUtils.copyProperties(po, vo);
            return vo;
        }).collect(Collectors.toList());
        return JsonResult.of(result);
    }
}
