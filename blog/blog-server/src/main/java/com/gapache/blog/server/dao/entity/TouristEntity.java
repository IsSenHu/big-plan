package com.gapache.blog.server.dao.entity;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/8/28 1:26 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_tourist")
public class TouristEntity extends BaseEntity<Long> {
    @Column(name = "email", unique = true)
    private String email;
    @Column(name = "nick", unique = true)
    private String nick;
    @Column(name = "password", nullable = false, length = 1024)
    private String password;
}
