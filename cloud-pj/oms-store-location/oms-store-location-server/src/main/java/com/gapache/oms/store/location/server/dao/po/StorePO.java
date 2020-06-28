package com.gapache.oms.store.location.server.dao.po;

import com.gapache.jpa.BaseEntity;
import com.gapache.oms.store.location.sdk.model.enums.StoreType;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author HuSen
 * @since 2020/6/24 11:26 上午
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "tb_store")
public class StorePO extends BaseEntity<Integer> {
    /**
     * 门店编码
     */
    @Column(name = "code", nullable = false, unique = true)
    private String code;
    /**
     * 门店名称
     */
    @Column(name = "name", nullable = false, unique = true)
    private String name;
    /**
     * 所在省
     */
    @Column(name = "province", nullable = false)
    private String province;
    /**
     * 所在市
     */
    @Column(name = "city", nullable = false)
    private String city;
    /**
     * 所在区
     */
    @Column(name = "area", nullable = false)
    private String area;
    /**
     * 详细地址
     */
    @Column(name = "address", nullable = false)
    private String address;
    /**
     * 纬度
     */
    @Column(name = "latitude", nullable = false, scale = 9)
    private Double latitude;
    /**
     * 经度
     */
    @Column(name = "longitude", nullable = false, scale = 9)
    private Double longitude;
    /**
     * 门店类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "store_type", nullable = false)
    private StoreType storeType;
}
