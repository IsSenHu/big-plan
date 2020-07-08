package com.spring.demo.java.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2020/7/8 11:38 下午
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TraceMethodInvoke {
}
