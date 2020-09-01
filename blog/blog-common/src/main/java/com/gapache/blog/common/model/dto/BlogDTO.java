package com.gapache.blog.common.model.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/5 03:27
 */
@Data
public class BlogDTO implements Serializable {
    private static final long serialVersionUID = -6355184113908752889L;

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
    private byte[] content;
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
