package com.gapache.blog.admin.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/5 03:44
 */
@Data
public class BlogUpdateVO implements Serializable {
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
    private LocalDateTime publishTime;
    /**
     * 分类
     */
    private String category;
    /**
     * 标签
     */
    private List<String> tags;
}
