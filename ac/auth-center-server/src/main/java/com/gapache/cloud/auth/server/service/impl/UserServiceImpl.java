package com.gapache.cloud.auth.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.dao.po.UserEntity;
import com.gapache.cloud.auth.server.dao.repository.UserRepository;
import com.gapache.cloud.auth.server.model.UserDetailsImpl;
import com.gapache.cloud.auth.server.service.UserService;
import com.gapache.security.exception.SecurityException;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.UserInfoDTO;
import com.gapache.security.model.UserLoginDTO;
import com.gapache.security.model.impl.CertificationImpl;
import com.gapache.security.properties.SignatureProperties;
import com.gapache.security.utils.JwtUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.PrivateKey;

/**
 * @author HuSen
 * @since 2020/7/31 10:19 上午
 */
@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private PrivateKey privateKey;

    @Resource
    private SignatureProperties signatureProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setUsername(userEntity.getUsername());
        userDetails.setPassword(userEntity.getPassword());
        return userDetails;
    }

    @Override
    public UserInfoDTO login(UserLoginDTO dto) {
        UserEntity userEntity = userRepository.findByUsername(dto.getUsername());
        if (userEntity == null) {
            throw new SecurityException(SecurityError.LOGIN_FAIL);
        }
        boolean passwordMatches = passwordEncoder.matches(dto.getPassword(), userEntity.getPassword());
        if (!passwordMatches) {
            throw new SecurityException(SecurityError.LOGIN_FAIL);
        }
        CertificationImpl certification = new CertificationImpl();
        certification.setId(userEntity.getId());
        certification.setName(userEntity.getUsername());

        String content = JSON.toJSONString(certification);
        String token = JwtUtils.generateToken(content, privateKey, signatureProperties.getTimeout());

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setToken(token);
        userInfoDTO.setNickname(userEntity.getUsername());
        return userInfoDTO;
    }
}
