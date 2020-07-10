package com.gapache.mybatis.demo.aspect;

import com.gapache.mybatis.demo.annotation.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/5/28 1:56 下午
 */
@Slf4j
@Aspect
@Component
public class AspectDemo {

    @Before("@annotation(logging)")
    public void log(JoinPoint joinPoint, Log logging) {
        log.info("this:{}", joinPoint.getThis());
        log.info("target:{}", joinPoint.getTarget());
        log.info("@annotation(logging)");
    }
}
