package com.gapahce.gateway.auth;

import com.dyuproject.protostuff.Tag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/6 22:44
 */
@Data
public class SessionInfo implements Serializable {
    private static final long serialVersionUID = 5368730405992667173L;

    @Tag(1)
    private Long userId;
    @Tag(2)
    private String username;
    @Tag(3)
    private String password;
    @Tag(4)
    private boolean isAccountNonLocked;
    @Tag(5)
    private boolean isEnabled;
    @Tag(6)
    private List<String> roles;
}
