package com.gapache.commons.model;

import lombok.Data;

import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/12 10:30
 */
@Data
public class ClientResponse {
    private String id;
    private Map<String, String> headers;
    private String body;
}
