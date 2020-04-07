package com.gapache.blog.server.dao.data;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/3 5:11 下午
 */
@Data
public class Tag implements Serializable {
    private static final long serialVersionUID = 4791213364030159082L;
    private String name;
    private Integer count;
}
