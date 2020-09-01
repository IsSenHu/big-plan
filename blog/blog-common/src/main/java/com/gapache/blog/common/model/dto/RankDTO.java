package com.gapache.blog.common.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/5 21:21
 */
@Data
public class RankDTO<T> implements Serializable {
    private static final long serialVersionUID = 4314073884097655385L;

    private Integer rank;
    private T data;
}
