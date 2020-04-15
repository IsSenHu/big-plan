package com.gapache.ngrok.commons;

import com.dyuproject.protostuff.Tag;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/11 3:38 下午
 */
@Data
public class ServerRequest {
    /**
     * 消息唯一ID
     */
    @Tag(1)
    private String id;
    /**
     * 请求目的地
     */
    @Tag(2)
    private String uri;
    /**
     * 请求类型
     */
    @Tag(3)
    private String method;
    /**
     * 协议版本
     */
    @Tag(4)
    private String protocolVersion;
    /**
     * 头信息
     */
    @Tag(5)
    private List<Map.Entry<String, String>> headers;
    /**
     * 尾头信息
     */
    @Tag(6)
    private List<Map.Entry<String, String>> trailingHeaders;
    /**
     * 内容
     */
    @Tag(7)
    private byte[] content;
}
