package com.gapache.user.server.dao.entity;

import com.gapache.jpa.OpFullRecordEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/9/8 11:23 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_user")
public class UserEntity extends OpFullRecordEntity<Long, String> {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false, length = 1024)
    private String password;

    @Column(name = "customize_info", length = 4096)
    private String customizeInfo;
}
