package com.gapache.cloud.auth.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.dao.entity.UserEntity;
import com.gapache.cloud.auth.server.dao.repository.ResourceRepository;
import com.gapache.cloud.auth.server.dao.repository.UserRepository;
import com.gapache.cloud.auth.server.model.UserDetailsImpl;
import com.gapache.cloud.auth.server.service.UserService;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.exception.SecurityException;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.UserDTO;
import com.gapache.security.model.UserInfoDTO;
import com.gapache.security.model.UserLoginDTO;
import com.gapache.security.model.impl.CertificationImpl;
import com.gapache.security.properties.SecurityProperties;
import com.gapache.security.utils.JwtUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.gapache.security.model.AuthConstants.TOKEN_HEADER;

/**
 * @author HuSen
 * @since 2020/7/31 10:19 上午
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final String MASTER = "husen";

    @Resource
    private UserRepository userRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private PrivateKey privateKey;

    @Resource
    private SecurityProperties securityProperties;

    @Resource
    private ResourceRepository resourceRepository;

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
        if (StringUtils.equals(MASTER, username)) {
            authorities = resourceRepository.findAll().stream()
                    .map(r -> new SimpleGrantedAuthority(r.getResourceServerName() + ":" + r.getScope()))
                    .collect(Collectors.toList());
        }
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

        // TODO保存token与token相关的用户信息

        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setToken(token);
        userInfoDTO.setName(userEntity.getUsername());
        userInfoDTO.setIntroduction("我是超级管理员");
        userInfoDTO.setRoles(Collections.singletonList("adminExclusive"));
        userInfoDTO.setAvatar("https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        return userInfoDTO;
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        String token = request.getHeader(TOKEN_HEADER);
        return StringUtils.isNotBlank(token);
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
            userDetails.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority("MoneyManagement:Test:checkAccessCard")));
            return userDetails;
        }).orElse(null);
    }
}
