package com.gapache.cloud.sdk;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/6/2 23:59
 */
@Data
public class PaymentVO implements Serializable {
    private static final long serialVersionUID = -3734328288280884268L;

    private Long id;
    private String serial;
}
