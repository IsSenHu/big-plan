package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.constant.GrantType;
import com.gapache.cloud.auth.server.constant.ResponseType;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.model.Certification;
import com.gapache.security.model.SecurityError;
import com.gapache.security.model.TokenInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivateKey;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author HuSen
 * @since 2020/7/31 5:20 下午
 */
@Slf4j
@Controller
@RequestMapping("/oauth")
public class Oauth2Controller {

    @Resource
    private ClientService clientService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private PrivateKey privateKey;

    private static final ConcurrentHashMap<String, String> CODE_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, String> CODE_USER_CACHE = new ConcurrentHashMap<>();

    private static final ConcurrentHashMap<String, Set<String>> SCOPES_CACHE = new ConcurrentHashMap<>();

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
                log.info("login failed");
                callback(redirectUri, response);
                return;
            }
            UserDetails userDetails = (UserDetails) authentication.getDetails();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            if (CollectionUtils.isEmpty(authorities) || !authorities.contains(new SimpleGrantedAuthority(scope))) {
                log.info("用户没有权限:{}, {}, {}", clientId, userDetails.getUsername(), scope);
                callback(redirectUri, response);
                return;
            }
            // TODO 生成授权码 暂时用UUID吧
            // TODO 保存clientId和userId已获得的scopes

            String clientAndUser = clientId + ":" + userDetails.getUsername();
            Set<String> userScopesOnCurrent = SCOPES_CACHE.computeIfAbsent(clientAndUser, k -> new HashSet<>());
            userScopesOnCurrent.add(scope);
            String code = UUID.randomUUID().toString();
            CODE_CACHE.put(code, redirectUri);
            CODE_USER_CACHE.put(code, userDetails.getUsername());
            callback(redirectUri + "?code=" + code, response);
        } else {
            callback(redirectUri, response);
        }
    }

    @PostMapping("/token")
    @ResponseBody
    public JsonResult<TokenInfoDTO> token(GrantType grantType, String clientId, String clientSecret, String code, String redirectUrl) {
        switch (grantType) {
            case implicit:
            case password:
            case refresh_token:
            case client_credentials: {
                break;
            }
            // 授权码模式
            case authorization_code: {
                String cacheRedirectUrl = null;
                ThrowUtils.throwIfTrue(StringUtils.isBlank(code) || (cacheRedirectUrl = CODE_CACHE.remove(code)) == null, SecurityError.ERROR_CODE);

                ClientDetailsImpl clientDetails = clientService.findByClientId(clientId);
                ThrowUtils.throwIfTrue(clientDetails == null ||
                        passwordEncoder.matches(clientSecret, clientDetails.getSecret()) ||
                        StringUtils.equals(cacheRedirectUrl, redirectUrl), SecurityError.CLIENT_ERROR);
                
                // 生成Token
                String username = CODE_USER_CACHE.get(code);
                String clientAndUser = clientId + ":" + username;
                Set<String> scopes = SCOPES_CACHE.computeIfAbsent(clientAndUser, k -> new HashSet<>());

                // 生成refresh token 如果支持的话
                break;
            }
            default: throw new SecurityException("not support grant type: " + grantType.name());
        }
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
