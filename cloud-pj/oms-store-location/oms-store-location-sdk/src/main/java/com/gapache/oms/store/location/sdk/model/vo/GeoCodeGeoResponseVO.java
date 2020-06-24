package com.gapache.oms.store.location.sdk.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 地址解析经纬度结果封装
 *
 * @author HuSen
 * @since 2020/6/24 3:05 下午
 */
@Data
public class GeoCodeGeoResponseVO implements Serializable {
    private static final long serialVersionUID = 8561816863823516881L;
    /**
     * 格式化后的地址
     */
    private String formattedAddress;
    /**
     * 国家
     */
    private String country;
    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 区
     */
    private String district;
    /**
     * 行政区划代码
     */
    private String adCode;
    /**
     * 纬度
     */
    private Double latitude;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * ??????不知道是啥东西
     */
    private String level;
}
