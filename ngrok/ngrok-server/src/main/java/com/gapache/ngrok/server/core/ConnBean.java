package com.gapache.ngrok.server.core;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author HuSen
 * @since 2020/8/14 9:58 上午
 */
@Data
@AllArgsConstructor
public class ConnBean {

    private final String host;
    private final String user;
    private final String password;
    private final Integer port;
}
