package com.gapache.oms.store.location.sdk.model.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 省
 *
 * @author HuSen
 * @since 2020/6/23 11:18 上午
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProvinceVO implements Serializable {
    private static final long serialVersionUID = -7346943405058037236L;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 管辖下的市
     */
    private List<CityVO> cities;
}
