package com.gapache.health.server.controller;

import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import com.gapache.health.server.model.body.BodyDataCreateVO;
import com.gapache.health.server.model.body.BodyDataQueryVO;
import com.gapache.health.server.model.body.BodyDataVO;
import com.gapache.health.server.model.body.BodyType;
import com.gapache.health.server.service.BodyDataService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/5/6 10:07 上午
 */
@RestController
@RequestMapping("/api/body/data")
public class BodyDataController {
    private final BodyDataService bodyDataService;

    public BodyDataController(BodyDataService bodyDataService) {
        this.bodyDataService = bodyDataService;
    }

    @PutMapping
    public JsonResult<Long> create(@RequestBody BodyDataCreateVO vo) {
        return bodyDataService.create(vo);
    }

    @GetMapping("/statistics")
    public JsonResult<List<BodyDataVO>> statistics() {
        return bodyDataService.statistics();
    }

    @PostMapping("/page")
    public JsonResult<PageResult<BodyDataVO>> page(@RequestBody IPageRequest<BodyDataQueryVO> request) {
        return bodyDataService.page(request);
    }

    @GetMapping("/bodyTypes")
    public JsonResult<List<Map<String, String>>> bodyTypes() {
        List<Map<String, String>> list = Arrays.stream(BodyType.values())
                .map(type -> {
                    Map<String, String> typeMap = new HashMap<>(2);
                    typeMap.put("id", type.name());
                    typeMap.put("name", type.getDesc());
                    return typeMap;
                })
                .collect(Collectors.toList());
        return JsonResult.of(list);
    }
}
