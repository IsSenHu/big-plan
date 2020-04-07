package com.gapache.blog.server.dao.data;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/4 22:36
 */
@Data
@AllArgsConstructor
public class Category implements Serializable {
    private static final long serialVersionUID = 4520772019101167184L;

    private String name;
}
