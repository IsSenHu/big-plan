package com.gapache.security.exception;

/**
 * @author HuSen
 * @since 2020/7/31 1:52 下午
 */
public class CertificationException extends RuntimeException {
    private static final long serialVersionUID = 2179249921376603875L;

    public CertificationException(String message) {
        super(message, null, false, false);
    }
}
