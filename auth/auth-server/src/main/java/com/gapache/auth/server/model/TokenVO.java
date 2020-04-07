package com.gapache.auth.server.model;

import lombok.Data;

/**
 * @author HuSen
 * create on 2019/8/29 9:57
 */
@Data
public class TokenVO {
    public TokenVO(String token, UserInfoVO info) {
        this.token = token;
        this.info = info;
    }

    private String token;
    private UserInfoVO info;
}
