package com.spring.demo.java;

import com.spring.demo.java.annotation.TraceMethodInvoke;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author HuSen
 * @since 2020/7/8 11:39 下午
 */
@Slf4j
@Aspect
@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class AspectConfiguration {

    @Before("@annotation(traceMethodInvoke)")
    public void beforeMethodInvoke(JoinPoint joinPoint, TraceMethodInvoke traceMethodInvoke) {
        log.info("invoke class:{}, method:{}", joinPoint.getTarget().getClass(), joinPoint.getSignature().getName());
    }
}
