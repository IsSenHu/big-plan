package com.gapache.cloud.seata.account.service.impl;

import com.gapache.cloud.seata.account.dao.po.AccountPO;
import com.gapache.cloud.seata.account.dao.repository.AccountRepository;
import com.gapache.cloud.seata.account.service.AccountService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 6:09 下午
 */
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Override
    public void debit(String userId, int money) {
        AccountPO account = accountRepository.findByUserId(userId);
        account.setMoney(account.getMoney() - money);
        accountRepository.save(account);
    }
}
