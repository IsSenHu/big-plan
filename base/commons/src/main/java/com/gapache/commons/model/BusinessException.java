package com.gapache.commons.model;

import lombok.Getter;

/**
 * @author HuSen
 * create on 2020/1/14 14:25
 */
@Getter
public class IException extends RuntimeException {
    private static final long serialVersionUID = -8570405796076634812L;

    private Error error;

    public IException(Error error) {
        super(null, null, false, false);
        this.error = error;
    }
}
