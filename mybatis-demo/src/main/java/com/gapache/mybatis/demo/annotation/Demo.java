package com.gapache.mybatis.demo.annotation;

import com.gapache.mybatis.demo.aware.DemoImportAware;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author HuSen
 * @since 2020/5/18 2:02 下午
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({DemoImportAware.class})
public @interface Demo {
    String value();
}
