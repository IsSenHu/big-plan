package com.gapache.oms.store.location.sdk.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/6/23 11:20 上午
 */
@Data
public class AreaVO implements Serializable {
    private static final long serialVersionUID = -3874417432894656092L;
    /**
     * 编码
     */
    private String code;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 名称
     */
    private String name;
}
