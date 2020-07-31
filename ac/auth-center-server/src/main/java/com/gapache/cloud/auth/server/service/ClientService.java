package com.gapache.cloud.auth.server.service;

import com.gapache.cloud.auth.server.model.ClientDetailsImpl;

/**
 * @author HuSen
 * @since 2020/7/31 5:24 下午
 */
public interface ClientService {

    ClientDetailsImpl findByClientId(String clientId);
}
