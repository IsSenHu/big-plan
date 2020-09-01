package com.gapache.blog.common.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/10 9:42 上午
 */
@Data
public class TagDTO implements Serializable {
    private static final long serialVersionUID = 8275741103787237451L;
    private String name;
    private Integer count;
}
