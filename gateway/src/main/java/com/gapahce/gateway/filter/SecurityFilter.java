package com.gapahce.gateway.filter;

import com.gapache.protobuf.utils.ProtocstuffUtils;
import com.gapache.security.checker.SecurityChecker;
import com.gapache.security.model.AccessCard;
import com.gapache.security.model.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * @since 2020/8/10 10:20 上午
 */
@Slf4j
@Component
public class SecurityFilter implements GlobalFilter, Ordered {

    private static final String TOKEN = "token";

    private final SecurityChecker securityChecker;

    public SecurityFilter(SecurityChecker securityChecker) {
        this.securityChecker = securityChecker;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst(TOKEN);
        if (StringUtils.isBlank(token)) {
            return chain.filter(exchange);
        }
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        AccessCard accessCard = securityChecker.checking(token);
        log.info("解析token花费时间:{}", stopWatch.getTotalTimeMillis());
        stopWatch.stop();

        if (accessCard != null) {
            byte[] message = ProtocstuffUtils.bean2Byte(accessCard, AccessCard.class);
            if (message != null) {
                String value = Base64Utils.encodeToString(message);
                log.info("{} :{}", AuthConstants.ACCESS_CARD_HEADER, value);
                exchange.getRequest().mutate()
                        .headers(httpHeaders -> httpHeaders.add(AuthConstants.ACCESS_CARD_HEADER, value));
            }
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
