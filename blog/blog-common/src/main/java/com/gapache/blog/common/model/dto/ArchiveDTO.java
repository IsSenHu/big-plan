package com.gapache.blog.common.model.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 归档
 *
 * @author HuSen
 * create on 2020/4/3 2:09 下午
 */
@Setter
@Getter
@ToString
public class ArchiveDTO implements Serializable {
    private static final long serialVersionUID = 1907358334210386896L;
    /**
     * 合计
     */
    private Long count;
    /**
     * 归档条目
     */
    private List<Map<String, Object>> timeline;
}
