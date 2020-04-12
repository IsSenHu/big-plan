package com.gapache.commons.model;

import lombok.Data;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/11 3:38 下午
 */
@Data
public class Message {
    /**
     * 消息唯一ID
     */
    private String id;
    /**
     * 请求目的地
     */
    private String destination;
    /**
     * 请求类型
     */
    private String method;
    /**
     * 头信息
     */
    private Map<String, String> headers;
    /**
     * 体
     */
    private String body;
}
