package com.gapache.blog.common.model.dto;

import com.dyuproject.protostuff.Tag;
import lombok.Data;

/**
 * @author HuSen
 * @since 2020/8/26 4:23 下午
 */
@Data
public class MarkdownItemDTO {
    @Tag(1)
    private Integer order;
    @Tag(2)
    private String type;
    @Tag(3)
    private Object content;
}
