package com.gapache.cloud.seata.account.service.impl;

import com.gapache.cloud.seata.account.dao.po.AccountPO;
import com.gapache.cloud.seata.account.dao.repository.AccountRepository;
import com.gapache.cloud.seata.account.error.AccountError;
import com.gapache.cloud.seata.account.service.AccountService;
import com.gapache.commons.model.ThrowUtils;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 6:09 下午
 */
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    @Resource
    private AccountRepository accountRepository;

    @Override
    public void debit(String userId, int money) {
        try {
            AccountPO account = accountRepository.findByUserId(userId);
            ThrowUtils.throwIfTrue(account.getMoney() < money, AccountError.INSUFFICIENT_BALANCE);
            account.setMoney(account.getMoney() - money);
            accountRepository.save(account);
        } catch (Exception e) {
            try {
                e.printStackTrace();
                GlobalTransactionContext.reload(RootContext.getXID()).rollback();
            } catch (TransactionException ex) {
                ex.printStackTrace();
            }
        }

    }
}
