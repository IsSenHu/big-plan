package com.gapache.blog.server.dao.entity;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * @author HuSen
 * @since 2020/8/27 7:30 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_review")
public class ReviewEntity extends BaseEntity<Long> {
    /**
     * 需要预留一些专用的ID
     *
     * 相关内容关联的ID
     */
    @Column(name = "other_id", nullable = false)
    private String otherId;
    /**
     * 谁评论的
     */
    @Column(name = "who", nullable = false)
    private String who;
    /**
     * 谁评论的ID
     */
    @Column(name = "who_id", nullable = false)
    private Long whoId;
    /**
     * 父评论 支持二级评论
     */
    @Column(name = "parent_id")
    private String parentId;
    /**
     * 这条回复关联的ID
     */
    @Column(name = "reply_relation_id")
    private String replyRelationId;
    /**
     * 具体内容
     */
    @Column(name = "content", nullable = false)
    private String content;
    /**
     * 发生的时间
     */
    @Column(name = "time")
    private LocalDateTime time;
}
