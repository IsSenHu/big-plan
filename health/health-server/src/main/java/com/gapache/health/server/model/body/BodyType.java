package com.gapache.health.server.model.body;

import lombok.Getter;

/**
 * @author HuSen
 * create on 2020/5/6 9:54 上午
 */
@Getter
public enum BodyType {
    // 隐性胖
    INVISIBLE_FAT("隐性胖"),
    // 偏胖型
    FATTY("偏胖型"),
    // 结实偏胖型
    STRONG_FAT("结实偏胖型"),
    // 缺少运动型
    LACK_OF_EXERCISE("缺少运动型"),
    // 标准型
    STANDARD("标准型"),
    // 标准肌肉型
    STANDARD_MUSCLE("标准肌肉型"),
    // 偏瘦型
    LEAN("偏瘦型"),
    // 偏瘦肌肉型
    LEAN_MUSCLE("偏瘦肌肉型"),
    // 健美肌肉型
    MUSCLE("健美肌肉型");

    private String desc;

    BodyType(String desc) {
        this.desc = desc;
    }
}
