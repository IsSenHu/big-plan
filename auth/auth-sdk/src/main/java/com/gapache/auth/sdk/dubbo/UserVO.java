package com.gapache.auth.sdk.dubbo;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @author HuSen
 * create on 2020/4/7 12:02 下午
 */
@Data
public class UserVO implements Serializable {
    private static final long serialVersionUID = -7932046364067413609L;

    private Long id;
    private String username;
    private String password;
    private Boolean isAccountNonLocked;
    private Boolean isEnabled;
    private Set<String> roles;
}
