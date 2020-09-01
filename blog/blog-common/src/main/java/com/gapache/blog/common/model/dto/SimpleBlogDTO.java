package com.gapache.blog.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/5 21:17
 */
@Data
public class SimpleBlogDTO implements Serializable {
    private static final long serialVersionUID = 6510707188800914385L;

    /**
     * id
     */
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 介绍
     */
    private String introduction;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;
    /**
     * 分类
     */
    private String category;
    /**
     * 标签
     */
    private String[] tags;
    /**
     * 阅读数量
     */
    private Integer views;
}
