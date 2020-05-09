package com.gapache.health.server.controller;

import com.gapache.commons.model.JsonResult;
import com.gapache.health.server.model.sleep.SleepCreateVO;
import com.gapache.health.server.service.SleepService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * create on 2020/5/9 9:25 上午
 */
@RestController
@RequestMapping("/api/sleep")
public class SleepController {

    private final SleepService sleepService;

    public SleepController(SleepService sleepService) {
        this.sleepService = sleepService;
    }

    @PutMapping
    public JsonResult<Long> create(@RequestBody SleepCreateVO vo) {
        return sleepService.create(vo);
    }
}
