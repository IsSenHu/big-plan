package com.gapache.blog.common.model.dto;

import com.dyuproject.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

/**
 * @author HuSen
 * @since 2020/8/26 7:09 下午
 */
@Getter
@Setter
public class MarkdownLinkDTO {
    @Tag(1)
    private String name;
    @Tag(2)
    private String link;

    @Override
    public String toString() {
        return name + link;
    }
}
