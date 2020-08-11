package com.gapache.security.filter;

import com.gapache.protobuf.utils.ProtocstuffUtils;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.model.AccessCard;
import com.gapache.security.model.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Base64Utils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author HuSen
 * @since 2020/8/9 5:44 下午
 */
@Slf4j
@WebFilter(filterName = "accessCardPersistenceFilter", urlPatterns = "/*")
public class AccessCardPersistenceFilter implements Filter {

    private static final String FILTER_APPLIED = "__acpf_applied";

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getAttribute(FILTER_APPLIED) != null) {
            // 确保一次请求中这个filter只应用一次
            chain.doFilter(request, response);
            return;
        }

        request.setAttribute(FILTER_APPLIED, Boolean.TRUE);

        String encoded = request.getHeader(AuthConstants.ACCESS_CARD_HEADER);
        if (StringUtils.isBlank(encoded)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            byte[] bytes = Base64Utils.decodeFromString(encoded);
            AccessCard accessCard = ProtocstuffUtils.byte2Bean(bytes, AccessCard.class);
            log.info("get access card from header:{}", accessCard);
            AccessCardHolder.setContext(accessCard);
            chain.doFilter(request, response);
        } finally {
            AccessCardHolder.clearContext();
            request.removeAttribute(FILTER_APPLIED);
            log.info("AccessCardHolder now cleared, as request processing completed");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) {
        log.info("init accessCardPersistenceFilter");
    }

    @Override
    public void destroy() {
        log.info("destroy accessCardPersistenceFilter");
    }
}
