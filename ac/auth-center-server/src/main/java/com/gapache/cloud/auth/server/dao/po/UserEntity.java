package com.gapache.cloud.auth.server.dao.po;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/7/31 10:21 上午
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "tb_user")
public class UserEntity extends BaseEntity<Long> {

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false, length = 1024)
    private String password;
}
