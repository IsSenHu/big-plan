package com.gapache.blog.common.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * @since 2020/8/28 2:29 下午
 */
@Data
public class ReviewDTO {
    /**
     * 需要预留一些专用的ID
     * 相关内容关联的ID
     */
    private String otherId;
    /**
     * 谁评论的
     */
    private String who;
    /**
     * 谁评论的ID
     */
    private Long whoId;
    /**
     * 父评论 支持二级评论
     */
    private String parentId;
    /**
     * 这条回复关联的ID
     */
    private String replyRelationId;
    /**
     * 具体内容
     */
    private String content;
    /**
     * 发生的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime time;
}
