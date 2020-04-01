package com.gapache.sms.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/14 16:12
 */

@Data
public class DeleteSmsSignVO implements Serializable {
    private static final long serialVersionUID = -5032267860001901116L;

    /**
     * 必须
     */
    private String signName;
}
