package com.gapache.user.service;

import java.lang.annotation.*;

/**
 * @author HuSen
 * create on 2019/7/18 10:35
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableLoadRedisLuaModule {
}
