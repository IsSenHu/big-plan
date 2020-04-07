package com.gapache.auth.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.gapache.auth.sdk.dubbo.AuthApiService;
import com.gapache.auth.sdk.dubbo.UserVO;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * create on 2020/4/7 12:22 下午
 */
@Slf4j
@Service
@Component
public class AuthApiServiceImpl implements AuthApiService {

    @Override
    public UserVO checkUser(String token) {
        log.info("check token:{}", token);
        UserVO user = new UserVO();
        user.setId(1L);
        user.setUsername("admin");
        user.setPassword("123456");
        user.setIsAccountNonLocked(true);
        user.setIsEnabled(true);
        user.setRoles(Sets.newHashSet("admin"));
        return user;
    }
}
