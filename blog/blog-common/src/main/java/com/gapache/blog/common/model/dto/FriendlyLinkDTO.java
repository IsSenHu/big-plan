package com.gapache.blog.common.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/8/28 9:38 上午
 */
@Data
public class FriendlyLinkDTO implements Serializable {
    private static final long serialVersionUID = -6817381987525951169L;

    private Integer id;
    private String name;
    private String avatar;
}
