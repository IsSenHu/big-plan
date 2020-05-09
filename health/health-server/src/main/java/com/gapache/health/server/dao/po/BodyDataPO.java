package com.gapache.health.server.dao.po;

import com.gapache.health.server.model.body.BodyType;
import com.gapache.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/5/6 9:49 上午
 */
@Getter
@Setter
@Entity
@Table(name = "tb_body_data")
public class BodyDataPO extends BaseEntity<Long> {
    /**
     * 身体得分
     */
    @Column(name = "body_score", nullable = false)
    private Integer bodyScore;
    /**
     * 检查时间
     */
    @Column(name = "check_time", nullable = false)
    private LocalDateTime checkTime;
    /**
     * 体重
     */
    @Column(name = "weight", nullable = false)
    private Double weight;
    /**
     * 体型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "body_type", nullable = false)
    private BodyType bodyType;
    /**
     * 水分
     */
    @Column(name = "moisture", nullable = false)
    private Double moisture;
    /**
     * 内脏脂肪
     */
    @Column(name = "visceral_fat", nullable = false)
    private Integer visceralFat;
    /**
     * bmi
     */
    @Column(name = "bmi", nullable = false)
    private Double bmi;
    /**
     * 体脂
     */
    @Column(name = "body_fat", nullable = false)
    private Double bodyFat;
    /**
     * 肌肉
     */
    @Column(name = "muscle", nullable = false)
    private Double muscle;
    /**
     * 蛋白质
     */
    @Column(name = "protein", nullable = false)
    private Double protein;
    /**
     * 基础代谢
     */
    @Column(name = "basal_metabolism", nullable = false)
    private Double basalMetabolism;
    /**
     * 骨量
     */
    @Column(name = "bone_mass", nullable = false)
    private Double boneMass;
    /**
     * 身体年龄
     */
    @Column(name = "body_age", nullable = false)
    private Integer bodyAge;
}
