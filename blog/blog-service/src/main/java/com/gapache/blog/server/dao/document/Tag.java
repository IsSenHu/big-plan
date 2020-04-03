package com.gapache.blog.server.dao.document;

import lombok.Data;

/**
 * @author HuSen
 * create on 2020/4/3 5:11 下午
 */
@Data
public class Tag {
    private String id;
    private String name;
    private Integer count;
}
