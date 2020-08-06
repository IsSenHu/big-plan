package com.gapache.cloud.auth.server.config;

import com.gapache.cloud.auth.server.security.AuthorizeInfoManager;
import com.gapache.cloud.auth.server.security.GenerateRefreshTokenStrategy;
import com.gapache.security.interfaces.GenerateTokenStrategy;
import com.gapache.cloud.auth.server.security.impl.JwtGenerateTokenStrategy;
import com.gapache.cloud.auth.server.security.impl.RedisAuthorizeInfoManager;
import com.gapache.cloud.auth.server.security.impl.UUIDGenerateRefreshTokenStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsUtils;

import javax.annotation.Resource;
import java.security.PrivateKey;

/**
 * @author HuSen
 * @since 2020/7/31 10:40 上午
 */
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    /**
     * 登录页面
     */
    private static final String LOGIN = "/login";

    /**
     * 登出地址
     */
    private static final String LOGOUT = "/logout";

    @Resource
    @Qualifier("userService")
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public GenerateTokenStrategy generateTokenStrategy(PrivateKey privateKey) {
        return new JwtGenerateTokenStrategy(privateKey);
    }

    @Bean
    public AuthorizeInfoManager authorizeInfoSaver(StringRedisTemplate stringRedisTemplate) {
        return new RedisAuthorizeInfoManager(stringRedisTemplate);
    }

    @Bean
    public GenerateRefreshTokenStrategy generateRefreshTokenStrategy() {
        return new UUIDGenerateRefreshTokenStrategy();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.headers().cacheControl();

        http.authorizeRequests()
                .antMatchers(HttpMethod.POST,"/auth/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/user").permitAll()
                .antMatchers(HttpMethod.POST,"/api/client").permitAll()
                .antMatchers(HttpMethod.POST, "/api/client/bindUser").permitAll()
                .antMatchers(HttpMethod.POST, "/oauth/token").permitAll()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage(LOGIN)
                .and()
                .logout().logoutUrl(LOGOUT).logoutSuccessUrl(LOGIN).permitAll();
    }

    private static final String CSS = "/css/**";
    private static final String JS = "/js/**";
    private static final String LIB = "/lib/**";
    private static final String IMAGES = "/images/**";
    private static final String FONTS = "/fonts/**";
    private static final String ELE_TREE = "/eleTree.js";
    private static final String FAVICON = "/favicon.ico";
    private static final String EXCEL = "/excel/**";
    private static final String LAY_EXT = "/layui_exts/**";
    private static final String LAY = "/layui/**";

    @Override
    public void configure(WebSecurity web) {
        // 不去拦截这些静态资源
        web.ignoring().antMatchers(IMAGES, LIB, CSS, FONTS, JS, ELE_TREE, FAVICON, EXCEL, LAY_EXT, LAY);
    }
}
