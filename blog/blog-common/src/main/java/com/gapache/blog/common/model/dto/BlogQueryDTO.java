package com.gapache.blog.common.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/7 4:20 下午
 */
@Data
public class BlogQueryDTO implements Serializable {
    private static final long serialVersionUID = 2388949042215749896L;
    private String tag;
    private String category;
}
