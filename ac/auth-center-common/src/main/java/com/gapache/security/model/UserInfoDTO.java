package com.gapache.security.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @author HuSen
 * @since 2020/7/31 2:04 下午
 */
@Data
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 3442985661989482222L;

    private String nickname;
    private String avatar;
    private String token;
}
