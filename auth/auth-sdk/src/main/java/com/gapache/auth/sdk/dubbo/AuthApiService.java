package com.gapache.auth.sdk.dubbo;

/**
 * @author HuSen
 * create on 2020/4/7 12:01 下午
 */
public interface AuthApiService {

    /**
     * 检查token并返回用户信息
     *
     * @param token 认证信息
     * @return 用户信息
     */
    UserVO checkUser(String token);
}
