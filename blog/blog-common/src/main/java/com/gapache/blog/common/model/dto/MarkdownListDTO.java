package com.gapache.blog.common.model.dto;

import com.dyuproject.protostuff.Tag;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/26 7:06 下午
 */
@Getter
@Setter
public class MarkdownListDTO {

    @Tag(1)
    private List<ListItemDTO> items;

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ListItemDTO item : items) {
            stringBuilder.append(item.getText());
        }
        return stringBuilder.toString();
    }
}
