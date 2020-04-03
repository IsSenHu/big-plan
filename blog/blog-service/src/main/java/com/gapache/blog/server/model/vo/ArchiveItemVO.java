package com.gapache.blog.server.model.vo;

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
public class ArchiveItemVO implements Serializable {
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
    private LocalDateTime publishTime;
}
