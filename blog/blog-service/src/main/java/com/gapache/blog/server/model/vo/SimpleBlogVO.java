package com.gapache.blog.server.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/5 21:17
 */
@Data
public class SimpleBlogVO implements Serializable {
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
    private LocalDateTime publishTime;
    /**
     * 分类
     */
    private String category;
    /**
     * 阅读数量
     */
    private Integer views;
}
