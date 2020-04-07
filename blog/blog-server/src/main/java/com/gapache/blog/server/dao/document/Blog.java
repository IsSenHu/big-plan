package com.gapache.blog.server.dao.document;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/3 1:04 下午
 */
@Data
public class Blog implements Serializable {
    private static final long serialVersionUID = 4014985602890356538L;
    /**
     * id
     */
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 文章内容
     */
    private String content;
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
     * 标签
     */
    private String[] tags;
}
