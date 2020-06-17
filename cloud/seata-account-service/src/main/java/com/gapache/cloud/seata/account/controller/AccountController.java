package com.gapache.cloud.seata.account.controller;

import com.gapache.cloud.seata.account.service.AccountService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 6:16 下午
 */
@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    @PostMapping("/debit")
    public void debit(String userId, int money) {
        accountService.debit(userId, money);
    }
}
