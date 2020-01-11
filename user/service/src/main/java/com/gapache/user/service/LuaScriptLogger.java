package com.gapache.user.service;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author HuSen
 * create on 2019/12/5 12:03
 */
@Slf4j
class LuaScriptLogger {
    private static final Logger LOGGER = LoggerFactory.getLogger(LuaScriptMap.class);

    static void error(String format, Object... arguments) {
        LOGGER.error(format, arguments);
    }
}
