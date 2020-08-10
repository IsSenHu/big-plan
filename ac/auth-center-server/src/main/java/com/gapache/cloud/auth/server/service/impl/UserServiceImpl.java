package com.gapache.cloud.auth.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.dao.entity.UserEntity;
import com.gapache.cloud.auth.server.dao.repository.UserRepository;
import com.gapache.cloud.auth.server.model.UserDetailsImpl;
import com.gapache.cloud.auth.server.service.UserService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.exception.SecurityException;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.UserDTO;
import com.gapache.security.model.UserInfoDTO;
import com.gapache.security.model.UserLoginDTO;
import com.gapache.security.model.impl.CertificationImpl;
import com.gapache.security.properties.SecurityProperties;
import com.gapache.security.utils.JwtUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author HuSen
 * @since 2020/7/31 10:19 上午
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private PrivateKey privateKey;

    @Resource
    private SecurityProperties securityProperties;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("用户名或密码不正确");
        }
        UserDetailsImpl userDetails = new UserDetailsImpl();
        userDetails.setId(userEntity.getId());
        userDetails.setUsername(userEntity.getUsername());
        userDetails.setPassword(userEntity.getPassword());
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("get_resource"));
        authorities.add(new SimpleGrantedAuthority("update_resource"));
        userDetails.setAuthorities(authorities);
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
        String token = JwtUtils.generateToken(content, privateKey, securityProperties.getTimeout());

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setToken(token);
        userInfoDTO.setNickname(userEntity.getUsername());
        return userInfoDTO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(UserDTO userDTO) {
        Boolean exists = userRepository.existsByUsername(userDTO.getUsername());
        ThrowUtils.throwIfTrue(exists, SecurityError.USER_EXISTED);

        UserEntity entity = new UserEntity();
        entity.setUsername(userDTO.getUsername());
        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userRepository.save(entity).getId() > 0;
    }

    @Override
    public UserDetailsImpl findById(Long id) {
        Optional<UserEntity> optional = userRepository.findById(id);
        return optional.map(userEntity -> {
            UserDetailsImpl userDetails = new UserDetailsImpl();
            userDetails.setId(userEntity.getId());
            userDetails.setUsername(userEntity.getUsername());
            userDetails.setPassword(userEntity.getPassword());
            userDetails.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("get_resource")));
            return userDetails;
        }).orElse(null);
    }
}
