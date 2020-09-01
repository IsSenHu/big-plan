package com.gapache.blog.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/4/3 2:29 下午
 */
@Setter
@Getter
@ToString
public class ArchiveItemDTO implements Serializable {
    private static final long serialVersionUID = -186942576354606727L;
    /**
     * id
     */
    private String id;
    /**
     * 标题
     */
    private String title;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime publishTime;
}
