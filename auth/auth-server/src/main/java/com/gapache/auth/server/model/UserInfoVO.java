package com.gapache.auth.server.model;

import lombok.Data;

import java.util.List;

/**
 * @author HuSen
 * create on 2019/8/27 16:34
 */
@Data
public class UserInfoVO {

    public UserInfoVO(String name, String introduction, String avatar, List<String> roles) {
        this.name = name;
        this.introduction = introduction;
        this.avatar = avatar;
        this.roles = roles;
    }

    /**
     * 用户名
     */
    private String name;

    /**
     * 用户说明和介绍
     */
    private String introduction;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 在 Vue Element admin 对应角色
     * 在 本系统中对应权限
     */
    private List<String> roles;
}
