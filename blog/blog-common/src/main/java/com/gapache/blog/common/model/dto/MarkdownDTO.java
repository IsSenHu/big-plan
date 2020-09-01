package com.gapache.blog.common.model.dto;

import com.dyuproject.protostuff.Tag;
import lombok.Data;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/26 7:15 下午
 */
@Data
public class MarkdownDTO {
    @Tag(1)
    private List<MarkdownItemDTO> items;
}
