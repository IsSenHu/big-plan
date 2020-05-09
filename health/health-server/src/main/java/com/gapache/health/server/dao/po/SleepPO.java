package com.gapache.health.server.dao.po;

import com.gapache.health.server.model.sleep.BedtimeState;
import com.gapache.health.server.model.sleep.DreamState;
import com.gapache.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/5/9 9:21 上午
 */
@Getter
@Setter
@Entity
@Table(name = "tb_sleep")
public class SleepPO extends BaseEntity<Long> {
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;
    /**
     * 入睡时间
     */
    @Column(name = "sleep_time", nullable = false)
    private LocalDateTime sleepTime;
    /**
     * 入睡时长 单位秒
     */
    @Column(name = "sleeping_time", nullable = false)
    private Integer sleepingTime;
    /**
     * 睡眠时长 单位秒
     */
    @Column(name = "sleeping_total_time", nullable = false)
    private Integer sleepingTotalTime;
    /**
     * 环境噪音 分贝
     */
    @Column(name = "ambient_noise", nullable = false)
    private Integer ambientNoise;
    /**
     * 睡眠年龄
     */
    @Column(name = "sleep_age", nullable = false)
    private Integer sleepAge;
    /**
     * 睡眠评分
     */
    @Column(name = "sleep_score", nullable = false)
    private Integer sleepScore;
    /**
     * 睡前状态
     * */
    @Enumerated(EnumType.STRING)
    @Column(name = "bedtime_state", nullable = false)
    private BedtimeState bedtimeState;
    /**
     * 梦境状态
     * */
    @Enumerated(EnumType.STRING)
    @Column(name = "dream_state", nullable = false)
    private DreamState dreamState;
}
