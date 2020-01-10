package com.gapache.user.service.dao.po;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * 账户持久化模型
 *
 * @author HuSen
 * create on 2020/1/8 18:03
 */
@Getter
@Setter
public class AccountPO {

    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String nickName;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDateTime createTime;
}
