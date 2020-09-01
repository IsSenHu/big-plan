package com.gapache.blog.common.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/10 9:41 上午
 */
@Data
public class CategoryDTO implements Serializable {
    private static final long serialVersionUID = 8708239822864960741L;
    private String name;

    public CategoryDTO() {
    }

    public CategoryDTO(String name) {
        this.name = name;
    }
}
