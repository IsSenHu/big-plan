package com.gapache.blog.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/5 03:44
 */
@Data
public class BlogUpdateDTO implements Serializable {
    private static final long serialVersionUID = 5318099956106605558L;

    /**
     * ID
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
    private String publishTime;
    /**
     * 分类
     */
    private String category;
    /**
     * 标签
     */
    private String tags;
}
