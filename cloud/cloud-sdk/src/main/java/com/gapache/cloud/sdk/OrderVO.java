package com.gapache.cloud.sdk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/6/17 4:25 下午
 */
@Data
public class OrderVO implements Serializable {
    private static final long serialVersionUID = 6251840262705030504L;

    private Long id;

    private String userId;

    private String orderBn;

    private String commodityCode;

    private Integer count;

    private Integer money;
}
