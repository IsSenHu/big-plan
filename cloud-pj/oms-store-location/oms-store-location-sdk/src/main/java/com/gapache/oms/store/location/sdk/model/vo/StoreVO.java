package com.gapache.oms.store.location.sdk.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 门店
 *
 * @author HuSen
 * @since 2020/6/24 11:23 上午
 */
@Data
public class StoreVO implements Serializable {
    private static final long serialVersionUID = 104429582003889554L;
    /**
     * 门店编码
     */
    private String code;
    /**
     * 门店名称
     */
    private String name;
    /**
     * 所在省
     */
    private String province;
    /**
     * 所在市
     */
    private String city;
    /**
     * 所在区
     */
    private String area;
    /**
     * 详细地址
     */
    private String address;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 经度
     */
    private Double longitude;
}
