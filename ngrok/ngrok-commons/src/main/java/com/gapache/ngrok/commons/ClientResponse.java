package com.gapache.ngrok.commons;

import com.dyuproject.protostuff.Tag;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/12 10:30
 */
@Data
public class ClientResponse {
    @Tag(1)
    private String messageId;
    @Tag(2)
    private int code;
    @Tag(3)
    private String reasonPhrase;
    @Tag(4)
    private String protocolVersion;
    @Tag(5)
    private List<Map.Entry<String, String>> headers;
    @Tag(6)
    private List<Map.Entry<String, String>> trailingHeaders;
    @Tag(7)
    private byte[] content;
}
