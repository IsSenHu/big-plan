package com.gapache.blog.server.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/5 21:21
 */
@Data
public class RankVO<T> implements Serializable {
    private static final long serialVersionUID = 4314073884097655385L;

    private Integer rank;
    private T data;
}
