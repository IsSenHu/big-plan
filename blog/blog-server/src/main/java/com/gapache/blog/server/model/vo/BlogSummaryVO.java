package com.gapache.blog.server.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/11 10:53 上午
 */
@Data
public class BlogSummaryVO implements Serializable {
    private static final long serialVersionUID = -1344573062942841731L;
    private String id;
    private String summary;
}
