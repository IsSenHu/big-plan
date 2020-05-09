package com.gapache.health.server.model.body;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/5/6 11:32 上午
 */
@Data
public class BodyDataVO implements Serializable {
    private static final long serialVersionUID = -7188866025965664668L;
    /**
     * ID
     */
    private Long id;
    /**
     * 身体得分
     */
    private Integer bodyScore;
    /**
     * 检查时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDateTime checkTime;
    /**
     * 体重
     */
    private Double weight;
    /**
     * 体型
     */
    private BodyType bodyType;
    /**
     * 水分
     */
    private Double moisture;
    /**
     * 内脏脂肪
     */
    private Integer visceralFat;
    /**
     * bmi
     */
    private Double bmi;
    /**
     * 体脂
     */
    private Double bodyFat;
    /**
     * 肌肉
     */
    private Double muscle;
    /**
     * 蛋白质
     */
    private Double protein;
    /**
     * 基础代谢
     */
    private Double basalMetabolism;
    /**
     * 骨量
     */
    private Double boneMass;
    /**
     * 身体年龄
     */
    private Integer bodyAge;
}
