package com.gapache.oms.store.location.sdk.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 市
 *
 * @author HuSen
 * @since 2020/6/23 11:19 上午
 */
@Data
public class CityVO implements Serializable {
    private static final long serialVersionUID = 834713854879292723L;
    /**
     * 编码
     */
    private String code;
    /**
     * 所属省编码
     */
    private String provinceCode;
    /**
     * 名称
     */
    private String name;
    /**
     * 管辖下的区
     */
    private List<AreaVO> areas;
}
