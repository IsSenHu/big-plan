package com.gapache.cloud.auth.server.service;

import com.gapache.security.model.UserLoginDTO;
import com.gapache.security.model.UserInfoDTO;

/**
 * @author HuSen
 * @since 2020/7/31 10:19 上午
 */
public interface UserService {

    /**
     * 用户登陆
     *
     * @param dto 用户登陆数据
     * @return 登陆结果
     */
    UserInfoDTO login(UserLoginDTO dto);
}
