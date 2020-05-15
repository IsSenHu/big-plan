package com.gapache.commons;

import java.util.logging.Logger;

/**
 * @author HuSen
 * @since 2020/5/15 6:50 下午
 */
public class LogTest {

    public static void main(String[] args) {
        // 用jdk的
        Logger logger = Logger.getLogger("神奇的log------------test");
        logger.info("some");
    }
}
