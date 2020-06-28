package com.gapache.oms.store.location.server.controller;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.StoreVO;
import com.gapache.oms.store.location.server.service.StoreService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/24 11:36 上午
 */
@RestController
@RequestMapping("/api/store")
public class StoreController {

    @Resource
    private StoreService storeService;

    @PostMapping
    public JsonResult<StoreVO> create(@RequestBody StoreVO store) {
        return storeService.create(store);
    }

    @GetMapping("/findClosestDistanceByAddress")
    public JsonResult<StoreVO> findClosestDistanceByAddress(@RequestParam(required = false) String city, String address) {
        return storeService.findClosestDistanceByAddress(city, address);
    }

    @GetMapping("/{code}")
    public JsonResult<StoreVO> findStore(@PathVariable("code") String code) {
        return storeService.findStore(code);
    }
}
