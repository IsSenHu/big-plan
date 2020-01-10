package com.gapache.user.service.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 最简单的注册方式，用户名密码注册 验证码进行验证
 *
 * @author HuSen
 * create on 2020/1/10 17:33
 */
@Data
public class AccountCreateRequest implements Serializable {

    private static final long serialVersionUID = -1011541610302142851L;

    private String username;

    private String password;

    private String phone;
}
