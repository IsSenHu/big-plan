package com.gapache.health.server.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.health.server.dao.po.SleepPO;
import com.gapache.health.server.dao.repository.SleepRepository;
import com.gapache.health.server.model.sleep.SleepCreateVO;
import com.gapache.health.server.service.SleepService;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * create on 2020/5/9 9:25 上午
 */
@Service
public class SleepServiceImpl implements SleepService {

    private final SleepRepository sleepRepository;

    public SleepServiceImpl(SleepRepository sleepRepository) {
        this.sleepRepository = sleepRepository;
    }

    @Override
    public JsonResult<Long> create(SleepCreateVO vo) {
        SleepPO po = new SleepPO();
        po.setUserId(1L);
        po.setAmbientNoise(vo.getAmbientNoise());
        po.setBedtimeState(vo.getBedtimeState());
        po.setDreamState(vo.getDreamState());
        po.setSleepAge(vo.getSleepAge());
        po.setSleepingTime(vo.getSleepingTime());
        po.setSleepingTotalTime(vo.getSleepingTotalTime());
        po.setSleepTime(vo.getSleepTime());
        po.setSleepScore(vo.getSleepScore());
        sleepRepository.save(po);
        return JsonResult.of(po.getId());
    }
}
