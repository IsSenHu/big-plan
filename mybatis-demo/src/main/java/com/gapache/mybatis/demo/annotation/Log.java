package com.gapache.mybatis.demo.annotation;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2020/5/28 2:00 下午
 */
@Inherited
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Log {

}
