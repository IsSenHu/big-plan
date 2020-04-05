package com.gapache.blog.server.model.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HuSen
 * create on 2020/4/5 22:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BlogVO extends SimpleBlogVO {
    private static final long serialVersionUID = -6865780194370949139L;

    private String content;
    private String[] tags;
}
