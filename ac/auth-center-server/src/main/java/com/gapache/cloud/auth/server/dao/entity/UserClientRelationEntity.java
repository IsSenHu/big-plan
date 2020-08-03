package com.gapache.cloud.auth.server.dao.entity;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/8/3 9:05 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table
public class UserClientRelationEntity extends BaseEntity<Long> {

    @Column(name = "user_id", nullable = false)
    private Long userId;
    @Column(name = "client_id", nullable = false)
    private Long clientId;
}
