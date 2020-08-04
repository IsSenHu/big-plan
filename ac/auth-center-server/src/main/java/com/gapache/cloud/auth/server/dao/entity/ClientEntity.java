package com.gapache.cloud.auth.server.dao.entity;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/7/31 4:54 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_client")
public class ClientEntity extends BaseEntity<Long> {

    @Column(name = "client_id", unique = true, nullable = false)
    private String clientId;

    @Column(name = "secret", nullable = false, length = 1024)
    private String secret;

    @Column(name = "grant_types")
    private String grantTypes;

    @Column(name = "scopes")
    private String scopes;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "timeout", nullable = false)
    private Long timeout;

    @Column(name = "refresh_token_timeout", nullable = false)
    private Long refreshTokenTimeout;
}
