package com.gapache.cloud.seata.account.dao.po;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/6/17 6:05 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_account")
public class AccountPO extends BaseEntity<Long> {

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "money")
    private Integer money;
}
