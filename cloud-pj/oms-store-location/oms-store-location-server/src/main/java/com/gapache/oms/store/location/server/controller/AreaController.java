package com.gapache.oms.store.location.server.controller;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.AreaVO;
import com.gapache.oms.store.location.sdk.model.vo.CityVO;
import com.gapache.oms.store.location.sdk.model.vo.ProvinceVO;
import com.gapache.oms.store.location.server.service.AreaService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/6/23 2:11 下午
 */
@RestController
@RequestMapping("/api/area")
public class AreaController {

    @Resource
    private AreaService areaService;

    @PostMapping("/refresh")
    public JsonResult<Object> refresh() {
        areaService.refresh();
        return JsonResult.success();
    }

    @GetMapping("/findAllProvince")
    public JsonResult<List<ProvinceVO>> findAllProvince(@RequestParam(required = false) String name) {
        return areaService.findAllProvince(name);
    }

    @GetMapping("/findAllCity")
    public JsonResult<List<CityVO>> findAllCity(String provinceCode, @RequestParam(required = false) String name) {
        return areaService.findAllCity(provinceCode, name);
    }

    @GetMapping("/findAllArea")
    public JsonResult<List<AreaVO>> findAllArea(String cityCode, @RequestParam(required = false) String name) {
        return areaService.findAllArea(cityCode, name);
    }
}
