package com.gapache.blog.server.dao.entity;

import com.gapache.blog.common.model.constant.FileType;
import com.gapache.jpa.OpTimeRecordEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2020/8/27 7:47 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_file")
public class FileEntity extends OpTimeRecordEntity<Long> {
    /**
     * 文件类型
     */
    @Column(name = "file_type")
    @Enumerated(EnumType.STRING)
    private FileType fileType;
    /**
     * 文件名称
     */
    @Column(name = "name", nullable = false)
    private String name;
    /**
     * 文件地址
     */
    @Column(name = "address")
    private String address;
    /**
     * 是否是安全文件，安全文件需要认证后才能访问
     */
    @Column(name = "security", length = 1)
    private Boolean security;
    /**
     * 关联的ID
     */
    @Column(name = "relation_id")
    private Long relationId;
}
