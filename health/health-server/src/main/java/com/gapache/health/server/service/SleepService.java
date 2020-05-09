package com.gapache.health.server.service;

import com.gapache.commons.model.JsonResult;
import com.gapache.health.server.model.sleep.SleepCreateVO;

/**
 * @author HuSen
 * create on 2020/5/9 9:25 上午
 */
public interface SleepService {
    /**
     * 创建睡眠记录
     *
     * @param vo 创建睡眠记录VO
     * @return 创建结果
     */
    JsonResult<Long> create(SleepCreateVO vo);
}
