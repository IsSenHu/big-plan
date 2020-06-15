package com.gapache.web;

import com.gapache.commons.model.IException;
import com.gapache.commons.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @author HuSen
 * create on 2020/1/14 17:54
 */
@Configuration
public class WebAutoConfiguration {

    @Slf4j
    @RestControllerAdvice
    public static class Advice {

        @ExceptionHandler(Exception.class)
        public JsonResult<String> exceptionHandler(Exception e) {
            log.error("请求发生异常:", e);
            if (e instanceof IException) {
                IException iE = (IException) e;
                return JsonResult.of(iE.getError());
            } else {
                return JsonResult.of(999999999, "未知异常");
            }
        }
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", buildConfig());
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

    private CorsConfiguration buildConfig() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        // 允许任何域名
        corsConfiguration.addAllowedOrigin("*");
        // 允许任何头
        corsConfiguration.addAllowedHeader("*");
        // 允许任何方法
        corsConfiguration.addAllowedMethod("*");
        return corsConfiguration;
    }
}
