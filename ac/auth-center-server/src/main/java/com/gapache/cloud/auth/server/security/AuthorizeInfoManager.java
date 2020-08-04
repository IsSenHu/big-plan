package com.gapache.cloud.auth.server.security;

import com.gapache.security.model.CustomerInfo;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/4 6:16 下午
 */
public interface AuthorizeInfoManager {

    /**
     * 保存授权后的信息
     *
     * @param token        accessToken
     * @param timeout      有效时长
     * @param customerInfo 用户自定义信息
     * @param scopes       作用域
     */
    void save(String token, Long timeout, CustomerInfo customerInfo, List<String> scopes);

    /**
     * 删除授权后的信息
     *
     * @param token token
     */
    void delete(String token);
}
