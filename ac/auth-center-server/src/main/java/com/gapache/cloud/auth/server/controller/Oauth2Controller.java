package com.gapache.cloud.auth.server.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.gapache.cloud.auth.server.constant.GrantType;
import com.gapache.cloud.auth.server.constant.ResponseType;
import com.gapache.cloud.auth.server.model.*;
import com.gapache.cloud.auth.server.security.AuthorizeInfoManager;
import com.gapache.cloud.auth.server.security.GenerateRefreshTokenStrategy;
import com.gapache.cloud.auth.server.security.GenerateTokenStrategy;
import com.gapache.cloud.auth.server.security.SecurityContextHelper;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private PrivateKey privateKey;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private GenerateTokenStrategy generateTokenStrategy;

    @Resource
    private AuthorizeInfoManager authorizeInfoManager;

    @Resource
    private GenerateRefreshTokenStrategy generateRefreshTokenStrategy;

    private static final String REFRESH_TOKEN_PREFIX = "refreshToken:";

    private static final String SCOPE_CACHE_PREFIX = "scopeCache:";

    private static final String CODE_CACHE_PREFIX = "codeCache:";

    @PostMapping("/userAuthorize")
    public void userAuthorize(UserAuthorizeDTO userAuthorizeDTO, HttpServletResponse response) {
        // 进到这里用户肯定是已经登陆了的
        UserDetailsImpl userDetails = SecurityContextHelper.getUserDetails();
        if (userDetails == null) {
            log.info("please login");
            callback("login", response);
            return;
        }

        String clientId = userDetails.getClientId();
        ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
        if (clientDetails == null) {
            log.info("clientDetails is null:{}", clientId);
            callback(userAuthorizeDTO.getRedirectUrl(), response);
            return;
        }
        List<String> scopeList = Arrays.stream(userAuthorizeDTO.getScopes().split(",")).collect(Collectors.toList());
        boolean anyMatch = scopeList.stream()
                .anyMatch(scope -> !userDetails.getAuthorities().contains(new SimpleGrantedAuthority(scope)) ||
                !clientDetails.getScopes().contains(scope));

        ThrowUtils.throwIfTrue(anyMatch, SecurityError.FORBIDDEN);

        String scopeCacheKey = SCOPE_CACHE_PREFIX + clientId + ":" + userDetails.getId();
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String scopeStr = JSON.toJSONString(new HashSet<>(scopeList));
        opsForValue.set(scopeCacheKey, scopeStr, clientDetails.getRefreshTokenTimeout(), TimeUnit.MILLISECONDS);

        String redirectUrl = userAuthorizeDTO.getRedirectUrl();
        if (StringUtils.isBlank(redirectUrl)) {
            redirectUrl = clientDetails.getRedirectUrl();
        }

        generateCode(redirectUrl, userDetails, response);
    }

    private void generateCode(String redirectUrl, UserDetailsImpl userDetails, HttpServletResponse response) {
        String code = UUID.randomUUID().toString().replace("-", "");
        CodeCacheInfoDTO codeCacheInfoDTO = new CodeCacheInfoDTO();
        codeCacheInfoDTO.setUserId(userDetails.getId());
        codeCacheInfoDTO.setUsername(userDetails.getUsername());
        codeCacheInfoDTO.setCustomerInfo(userDetails.getCustomerInfo());
        // code有效时长为5分钟
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        opsForValue.set(CODE_CACHE_PREFIX + code, JSON.toJSONString(codeCacheInfoDTO), 5, TimeUnit.MINUTES);
        callback(redirectUrl + "?code=" + code, response);
    }

    @GetMapping("/authorize")
    public void authorize(ResponseType responseType, String clientId, String redirectUrl, String scope, HttpServletResponse response) {
        ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
        if (clientDetails == null) {
            log.info("client不存在:{}", clientId);
            callback(redirectUrl, response);
            return;
        }
        redirectUrl = StringUtils.isNotBlank(redirectUrl) ? redirectUrl : clientDetails.getRedirectUrl();
        if (responseType.equals(ResponseType.code)) {
            if (!clientDetails.getGrantTypes().contains(GrantType.authorization_code)) {
                log.info("只支持授权码模式:{}", clientId);
                callback(redirectUrl, response);
                return;
            }
            if (!clientDetails.getScopes().contains(scope)) {
                log.info("该client不拥有该scope:{}, {}", clientId, scope);
                callback(redirectUrl, response);
                return;
            }
            // 检查用户是否有该权限
            UserDetailsImpl userDetails  = SecurityContextHelper.getUserDetails();
            if (userDetails == null) {
                log.info("please login");
                callback(redirectUrl, response);
                return;
            }
            userDetails.setClientId(clientId);

            UserClientRelationDTO userClientRelationDTO = userClientRelationService.findByUserIdAndClientId(userDetails.getId(), clientDetails.getId());
            if (userClientRelationDTO == null) {
                log.info("用户不属于该client:{}, {}, {}", clientId, userDetails.getId(), userDetails.getUsername());
                callback(redirectUrl, response);
                return;
            }
            if (CollectionUtils.isEmpty(userDetails.getAuthorities()) || !userDetails.getAuthorities().contains(new SimpleGrantedAuthority(scope))) {
                log.info("用户没有权限:{}, {}, {}, {}", clientId, userDetails.getId(), userDetails.getUsername(), scope);
                callback(redirectUrl, response);
                return;
            }

            // oauth2是需要用户每次去授权的，一个client和user同时只会存在一个token
            ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
            String scopeCacheKey = SCOPE_CACHE_PREFIX + clientId + ":" + userDetails.getId();
            // 如果已经有这个权限了则自动授权
            String scopeCacheStr = opsForValue.get(scopeCacheKey);
            if (StringUtils.isNotBlank(scopeCacheStr) && JSONArray.parseArray(scopeCacheStr, String.class).contains(scope)) {
                generateCode(redirectUrl, userDetails, response);
            }
            // 没有的话则需要到确认授权页面进行授权
            else {
                callback("/oauth/manualAuthorize?type=1&scopes=" + scope + "&redirectUrl=" + redirectUrl, response);
            }
        } else {
            callback(redirectUrl, response);
        }
    }

    @PostMapping("/token")
    @ResponseBody
    public JsonResult<TokenInfoDTO> token(GrantType grantType, String clientId, String clientSecret, String code, String refreshToken) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        Map<String, Object> generateTokenParams = new HashMap<>(4);
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
                UserDetailsImpl userDetails = userService.findById(refreshTokenDTO.getUserId());

                UserClientRelationDTO userClientRelationDTO = userClientRelationService.findByUserIdAndClientId(userDetails.getId(), clientDetails.getId());
                ThrowUtils.throwIfTrue(userClientRelationDTO == null, SecurityError.CLIENT_ERROR);

                // 删除旧token
                authorizeInfoManager.delete(refreshTokenDTO.getAccessToken());

                buildGenerateTokenParams(generateTokenParams, clientDetails.getTimeout(), userDetails.getUsername(), userDetails.getId());
                String token = generateTokenStrategy.generate(generateTokenParams);

                List<String> scopes = loadAuthorizedScope(clientId, userDetails.getId());
                authorizeInfoManager.save(token, clientDetails.getTimeout(), userDetails.getCustomerInfo(), scopes);

                TokenInfoDTO dto = new TokenInfoDTO();
                dto.setAccessToken(token);
                // 生成refresh token 如果支持的话
                Long refreshTokenTimeout = clientDetails.getRefreshTokenTimeout();
                if (clientDetails.getGrantTypes().contains(GrantType.refresh_token) && refreshTokenTimeout > 0) {
                    String newRefreshToken = generateRefreshToken(refreshToken, userDetails.getId(), token, refreshTokenTimeout);
                    dto.setRefreshToken(newRefreshToken);
                }
                return JsonResult.of(dto);
            }
            case client_credentials: {
                break;
            }
            // 授权码模式
            case authorization_code: {
                ThrowUtils.throwIfTrue(StringUtils.isBlank(code), SecurityError.ERROR_CODE);
                String codeInfoStr = opsForValue.get(CODE_CACHE_PREFIX + code);
                ThrowUtils.throwIfTrue(StringUtils.isBlank(codeInfoStr), SecurityError.ERROR_CODE);
                CodeCacheInfoDTO codeCacheInfoDTO = JSON.parseObject(codeInfoStr, CodeCacheInfoDTO.class);

                ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
                ThrowUtils.throwIfTrue(clientDetails == null ||
                        !passwordEncoder.matches(clientSecret, clientDetails.getSecret()), SecurityError.CLIENT_ERROR);

                // 生成token对应的内容
                buildGenerateTokenParams(generateTokenParams, clientDetails.getTimeout(), codeCacheInfoDTO.getUsername(), codeCacheInfoDTO.getUserId());
                String token = generateTokenStrategy.generate(generateTokenParams);

                // 保存token对应的信息
                List<String> scopes = loadAuthorizedScope(clientId, codeCacheInfoDTO.getUserId());
                authorizeInfoManager.save(token, clientDetails.getTimeout(), codeCacheInfoDTO.getCustomerInfo(), scopes);

                TokenInfoDTO dto = new TokenInfoDTO();
                dto.setAccessToken(token);
                // 生成refresh token 如果支持的话
                Long refreshTokenTimeout = clientDetails.getRefreshTokenTimeout();
                if (clientDetails.getGrantTypes().contains(GrantType.refresh_token) && refreshTokenTimeout > 0) {
                    String newRefreshToken = generateRefreshToken(refreshToken, codeCacheInfoDTO.getUserId(), token, refreshTokenTimeout);
                    dto.setRefreshToken(newRefreshToken);
                }
                return JsonResult.of(dto);
            }
            default: throw new SecurityException(SecurityError.NOT_SUPPORT);
        }
        return null;
    }

    private String generateRefreshToken(String oldRefreshToken, Long userId, String token, Long refreshTokenTimeout) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String newRefreshToken = generateRefreshTokenStrategy.generate(null);
        RefreshTokenDTO refreshTokenDTO = new RefreshTokenDTO();
        refreshTokenDTO.setUserId(userId);
        refreshTokenDTO.setAccessToken(token);
        // 删除旧的refresh token
        if (StringUtils.isNotBlank(oldRefreshToken)) {
            stringRedisTemplate.delete(oldRefreshToken);
        }
        // 保存新的refresh token
        opsForValue.setIfAbsent(REFRESH_TOKEN_PREFIX + newRefreshToken, JSON.toJSONString(refreshTokenDTO), refreshTokenTimeout, TimeUnit.MILLISECONDS);
        return newRefreshToken;
    }

    private List<String> loadAuthorizedScope(String clientId, Long userId) {
        ValueOperations<String, String> opsForValue = stringRedisTemplate.opsForValue();
        String scopeCacheStr = opsForValue.get(SCOPE_CACHE_PREFIX + clientId + ":" + userId);
        List<String> scopes = new ArrayList<>();
        if (StringUtils.isNotBlank(scopeCacheStr)) {
            scopes = JSONArray.parseArray(scopeCacheStr, String.class);
        }
        return scopes;
    }

    private void buildGenerateTokenParams(Map<String, Object> generateTokenParams, Long timeout, String username, Long id) {
        CertificationImpl certification = new CertificationImpl();
        certification.setName(username);
        certification.setId(id);
        String content = JSON.toJSONString(certification);

        generateTokenParams.put("content", content);
        generateTokenParams.put("privateKey", privateKey);
        generateTokenParams.put("timeout", timeout);
    }

    @PostMapping("/check")
    public JsonResult<Certification> check() {
        return null;
    }

    private void callback(String redirectUrl, HttpServletResponse response) {
        try {
            response.sendRedirect(redirectUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
