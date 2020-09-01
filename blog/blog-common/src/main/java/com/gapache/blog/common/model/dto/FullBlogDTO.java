package com.gapache.blog.common.model.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/5 22:11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class FullBlogDTO extends SimpleBlogDTO {
    private static final long serialVersionUID = -6865780194370949139L;

    private List<MarkdownItemDTO> content;
}
