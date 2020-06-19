package com.gapache.oms.order.base.model.vo.order;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 优惠详情
 *
 * @author HuSen
 * @since 2020/6/19 10:21 上午
 */
@Data
public class PmtDetailVO implements Serializable {
    private static final long serialVersionUID = 4737698422244013922L;
    /**
     * 优惠价格
     */
    private BigDecimal pmtAmount;
    /**
     * 优惠说明
     */
    private String pmtMemo;
}
