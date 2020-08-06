package com.gapache.cloud.auth.server.service;

import com.gapache.cloud.auth.server.model.UserDetailsImpl;
import com.gapache.security.model.UserDTO;
import com.gapache.security.model.UserLoginDTO;
import com.gapache.security.model.UserInfoDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author HuSen
 * @since 2020/7/31 10:19 上午
 */
public interface UserService extends UserDetailsService {

    /**
     * 用户登陆
     *
     * @param dto 用户登陆数据
     * @return 登陆结果
     */
    UserInfoDTO login(UserLoginDTO dto);

    Boolean create(UserDTO userDTO);

    UserDetailsImpl findById(Long id);
}
