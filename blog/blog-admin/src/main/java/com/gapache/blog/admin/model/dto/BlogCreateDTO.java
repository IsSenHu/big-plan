package com.gapache.blog.admin.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/5 03:44
 */
@Data
public class BlogCreateDTO implements Serializable {
    private static final long serialVersionUID = 5318099956106605558L;

    /**
     * 标题
     */
    private String title;
    /**
     * 介绍
     */
    private String introduction;
    /**
     * 分类
     */
    private String category;
    /**
     * 标签
     */
    private String tags;
}
