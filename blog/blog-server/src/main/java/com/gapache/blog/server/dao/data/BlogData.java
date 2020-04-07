package com.gapache.blog.server.dao.data;

import com.dyuproject.protostuff.Tag;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/5 22:15
 */
@Data
public class BlogData implements Serializable {
    private static final long serialVersionUID = -8038461718931073309L;

    /**
     * id
     */
    @Tag(1)
    private String id;
    /**
     * 标题
     */
    @Tag(2)
    private String title;
    /**
     * 文章内容
     */
    @Tag(3)
    private byte[] content;
    /**
     * 介绍
     */
    @Tag(4)
    private String introduction;
    /**
     * 发布时间
     */
    @Tag(5)
    private LocalDateTime publishTime;
    /**
     * 分类
     */
    @Tag(6)
    private String category;
    /**
     * 标签
     */
    @Tag(7)
    private String[] tags;
}
