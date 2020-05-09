package com.gapache.health.server.model.sleep;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/5/9 9:35 上午
 */
@Data
public class SleepCreateVO implements Serializable {
    private static final long serialVersionUID = -5697751450942262281L;
    /**
     * 入睡时间
     */
    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime sleepTime;
    /**
     * 入睡时长 单位秒
     */
    @NotNull
    private Integer sleepingTime;
    /**
     * 睡眠时长 单位秒
     */
    @NotNull
    private Integer sleepingTotalTime;
    /**
     * 环境噪音 分贝
     */
    @NotNull
    private Integer ambientNoise;
    /**
     * 睡眠年龄
     */
    @NotNull
    private Integer sleepAge;
    /**
     * 睡眠评分
     */
    @NotNull
    private Integer sleepScore;
    /**
     * 睡前状态
     * */
    @NotNull
    private BedtimeState bedtimeState;
    /**
     * 梦境状态
     * */
    @NotNull
    private DreamState dreamState;
}
