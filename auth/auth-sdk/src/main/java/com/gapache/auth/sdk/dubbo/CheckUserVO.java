package com.gapache.auth.sdk.dubbo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/7 12:03 下午
 */
@Data
public class CheckUserVO implements Serializable {
    private static final long serialVersionUID = -5701892408602664636L;

    private String username;
    private String password;
}
