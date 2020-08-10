package com.gapache.cloud.auth.server.dao.entity;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/8/6 4:08 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_resource")
public class ResourceEntity extends BaseEntity<Long> {

    @Column(name = "resource_server_name", nullable = false)
    private String resourceServerName;

    @Column(name = "scope", nullable = false)
    private String scope;

    @Column(name = "name", nullable = false)
    private String name;
}
