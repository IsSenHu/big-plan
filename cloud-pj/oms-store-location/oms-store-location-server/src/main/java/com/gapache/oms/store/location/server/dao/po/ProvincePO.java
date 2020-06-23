package com.gapache.oms.store.location.server.dao.po;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/6/23 2:03 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_province")
public class ProvincePO extends BaseEntity<Integer> {
    /**
     * 省编码
     */
    @Column(name = "code", unique = true, nullable = false)
    private String code;
    /**
     * 名称
     */
    @Column(name = "name", unique = true, nullable = false)
    private String name;
}
