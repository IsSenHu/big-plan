package com.gapahce.gateway.auth;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gapache.auth.sdk.dubbo.AuthApiService;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * create on 2020/4/7 11:56 上午
 */
@Component
public class AuthProcessor {

    @Reference(check = false)
    private AuthApiService authApiService;

    public void checkUser() {

    }
}
