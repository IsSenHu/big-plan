package com.gapache.cloud.seata.storage.dao.po;

import com.gapache.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author HuSen
 * @since 2020/6/17 5:21 下午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_storage")
public class StoragePO extends BaseEntity<Long> {

    @Column(name = "commodity_code", nullable = false, unique = true)
    private String commodityCode;

    @Column
    private Integer count;
}
