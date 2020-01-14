package com.gapache.sms.server.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/1/14 16:40
 */
@Data
public class DeleteSmsTemplateVO implements Serializable {
    private static final long serialVersionUID = 8479525720715358448L;

    /**
     * 必须
     */
    private String templateCode;
}
