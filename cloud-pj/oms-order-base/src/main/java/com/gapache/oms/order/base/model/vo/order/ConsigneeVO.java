package com.gapache.oms.order.base.model.vo.order;

import lombok.Data;

import java.io.Serializable;

/**
 * 收货地址信息
 *
 * @author HuSen
 * @since 2020/6/18 6:11 下午
 */
@Data
public class ConsigneeVO implements Serializable {
    private static final long serialVersionUID = 4550097509451641522L;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 电话
     * */
    private String telephone;
    /**
     * 收货人姓名
     */
    private String name;
    /**
     * 收货详细地址
     */
    private String addr;
    /**
     * 省
     */
    private String areaState;
    /**
     * 市
     */
    private String areaCity;
    /**
     * 区
     */
    private String areaDistrict;
}
