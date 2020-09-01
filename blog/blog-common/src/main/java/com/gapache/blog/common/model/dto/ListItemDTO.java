package com.gapache.blog.common.model.dto;

import com.dyuproject.protostuff.Tag;
import lombok.Data;

/**
 * @author HuSen
 * @since 2020/8/26 5:02 下午
 */
@Data
public class ListItemDTO {
    @Tag(1)
    private Integer order;
    @Tag(2)
    private String text;
}
