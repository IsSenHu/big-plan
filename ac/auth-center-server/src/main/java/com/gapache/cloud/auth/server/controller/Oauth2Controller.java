package com.gapache.cloud.auth.server.controller;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.constant.GrantType;
import com.gapache.cloud.auth.server.constant.ResponseType;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.model.RefreshTokenDTO;
import com.gapache.cloud.auth.server.model.UserClientRelationDTO;
import com.gapache.cloud.auth.server.model.UserDetailsImpl;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.cloud.auth.server.service.UserClientRelationService;
import com.gapache.cloud.auth.server.service.UserService;
import com.gapache.commons.model.Error;
import com.gapache.commons.model.*;
import com.gapache.security.exception.SecurityException;
import com.gapache.security.model.Certification;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.TokenInfoDTO;
import com.gapache.security.model.impl.CertificationImpl;
import com.gapache.security.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author HuSen
 * @since 2020/7/31 5:20 下午
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
public class Oauth2Controller {

    @ExceptionHandler
    @ResponseBody
    public JsonResult<Object> exceptionHandler(Exception e) {
        if (e instanceof SecurityException) {
            SecurityException securityException = (SecurityException) e;
            Error error = securityException.getError();
            log.error("SecurityException:{}, {}", error.getCode(), error.getError());
            return JsonResult.of(error);
        } else if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            Error error = businessException.getError();
            log.error("BusinessException:{}, {}", error.getCode(), error.getError());
            return JsonResult.of(error);
        } else {
            log.error("Oauth2Controller error ", e);
            return JsonResult.of(SystemError.SERVER_EXCEPTION);
        }
    }

    @Resource
    private ClientService clientService;

    @Resource
    private UserClientRelationService userClientRelationService;

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private PrivateKey privateKey;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final ConcurrentHashMap<String, String> CODE_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, String> CODE_USER_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, Set<String>> SCOPES_CACHE = new ConcurrentHashMap<>();

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    @GetMapping("/authorize")
    public void authorize(ResponseType responseType, String clientId, String redirectUri, String scope, HttpServletResponse response) {
        ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
        if (clientDetails == null) {
            log.info("client不存在:{}", clientId);
            callback(redirectUri, response);
            return;
        }
        redirectUri = StringUtils.isNotBlank(redirectUri) ? redirectUri : clientDetails.getRedirectUrl();
        if (responseType.equals(ResponseType.code)) {
            if (!clientDetails.getGrantTypes().contains(GrantType.authorization_code)) {
                log.info("不支持授权码模式:{}", clientId);
                callback(redirectUri, response);
                return;
            }
            if (!clientDetails.getScopes().contains(scope)) {
                log.info("该client不拥有该scope:{}, {}", clientId, scope);
                callback(redirectUri, response);
                return;
            }
            // 检查用户是否有该权限
            SecurityContext context = SecurityContextHolder.getContext();
            Authentication authentication = context.getAuthentication();
            if (authentication == null) {
                log.info("please login");
                callback(redirectUri, response);
                return;
            }
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            UserClientRelationDTO userClientRelationDTO = userClientRelationService.findByUserIdAndClientId(userDetails.getId(), clientDetails.getId());
            if (userClientRelationDTO == null) {
                log.info("用户不属于该client:{}, {}, {}", clientId, userDetails.getId(), userDetails.getUsername());
                callback(redirectUri, response);
            }
            if (CollectionUtils.isEmpty(userDetails.getAuthorities()) || !userDetails.getAuthorities().contains(new SimpleGrantedAuthority(scope))) {
                log.info("用户没有权限:{}, {}, {}, {}", clientId, userDetails.getId(), userDetails.getUsername(), scope);
                callback(redirectUri, response);
                return;
            }

            String clientAndUser = clientId + ":" + userDetails.getUsername();
            Set<String> userScopesOnCurrent = SCOPES_CACHE.computeIfAbsent(clientAndUser, k -> new HashSet<>());
            userScopesOnCurrent.add(scope);

            String code = UUID.randomUUID().toString().replace("-", "");
            CODE_CACHE.put(code, redirectUri);
            CODE_USER_CACHE.put(code, userDetails.getUsername());
            callback(redirectUri + "?code=" + code, response);
        } else {
            callback(redirectUri, response);
        }
    }

    @PostMapping("/token")
    @ResponseBody
    public JsonResult<TokenInfoDTO> token(GrantType grantType, String clientId, String clientSecret, String code, String redirectUrl, String refreshToken) {
        switch (grantType) {
            case implicit:
            case password:
            case refresh_token: {
                ThrowUtils.throwIfTrue(StringUtils.isBlank(refreshToken), SecurityError.NEED_REFRESH_TOKEN);

                ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
                ThrowUtils.throwIfTrue(clientDetails == null ||
                        !passwordEncoder.matches(clientSecret, clientDetails.getSecret()), SecurityError.CLIENT_ERROR);

                String refreshTokenDtoStr = stringRedisTemplate.opsForValue().get(REFRESH_TOKEN_PREFIX + refreshToken);
                ThrowUtils.throwIfTrue(StringUtils.isBlank(refreshTokenDtoStr), SecurityError.REFRESH_TOKEN_EXPIRED);

                RefreshTokenDTO refreshTokenDTO = JSON.parseObject(refreshTokenDtoStr, RefreshTokenDTO.class);

                break;
            }
            case client_credentials: {
                break;
            }
            // 授权码模式
            case authorization_code: {
                String cacheRedirectUrl = null;
                ThrowUtils.throwIfTrue(StringUtils.isBlank(code) || (cacheRedirectUrl = CODE_CACHE.get(code)) == null, SecurityError.ERROR_CODE);

                ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
                ThrowUtils.throwIfTrue(clientDetails == null ||
                        !passwordEncoder.matches(clientSecret, clientDetails.getSecret()) ||
                        !StringUtils.equals(cacheRedirectUrl, redirectUrl), SecurityError.CLIENT_ERROR);
                
                // 生成Token
                String username = CODE_USER_CACHE.get(code);
                String clientAndUser = clientId + ":" + username;
                Set<String> scopes = SCOPES_CACHE.computeIfAbsent(clientAndUser, k -> new HashSet<>());

                UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(username);
                CertificationImpl certification = new CertificationImpl();
                certification.setName(username);
                certification.setAuthorities(new ArrayList<>(scopes));
                certification.setId(userDetails.getId());

                String content = JSON.toJSONString(certification);
                String token = JwtUtils.generateToken(content, privateKey, clientDetails.getTimeout());

                TokenInfoDTO dto = new TokenInfoDTO();
                dto.setToken(token);
                // 生成refresh token 如果支持的话
                if (clientDetails.getGrantTypes().contains(GrantType.refresh_token) && clientDetails.getRefreshTokenTimeout() != 0) {
                    ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
                    RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
                    refreshTokenDTO.setUserId(userDetails.getId());
                    refreshTokenDTO.setClientId(clientDetails.getId());
                    String newRefreshToken = UUID.randomUUID().toString().replace("-", "");
                    dto.setRefreshToken(newRefreshToken);
                    if (clientDetails.getRefreshTokenTimeout() != -1) {
                        opsForValue.setIfAbsent(REFRESH_TOKEN_PREFIX + newRefreshToken, JSON.toJSONString(refreshTokenDTO), clientDetails.getTimeout(), TimeUnit.MILLISECONDS);
                    } else {
                        opsForValue.setIfAbsent(REFRESH_TOKEN_PREFIX + newRefreshToken, JSON.toJSONString(refreshTokenDTO));
                    }
                }
                return JsonResult.of(dto);
            }
            default: throw new SecurityException(SecurityError.NOT_SUPPORT);
        }
        return null;
    }

    @PostMapping("/check")
    public JsonResult<Certification> check() {
        return null;
    }

    private void callback(String redirectUri, HttpServletResponse response) {
        try {
            response.sendRedirect(redirectUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
