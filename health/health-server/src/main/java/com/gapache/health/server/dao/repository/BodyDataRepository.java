package com.gapache.health.server.dao.repository;

import com.gapache.health.server.dao.po.BodyDataPO;
import com.gapache.jpa.BaseJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/5/6 10:05 上午
 */
public interface BodyDataRepository extends BaseJpaRepository<BodyDataPO, Long> {

    /**
     * 查询检查时间位于start和end之间的身体数据
     *
     * @param start 开始时间
     * @param end 结束时间
     * @return 查询结果
     */
    List<BodyDataPO> findAllByCheckTimeLessThanEqualAndCheckTimeGreaterThanEqual(LocalDateTime end, LocalDateTime start);
}
