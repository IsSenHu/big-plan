package com.gapache.user.service.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.user.service.dao.repository.AccountRepository;
import com.gapache.user.service.model.AccountRegisterRequest;
import com.gapache.user.service.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * create on 2020/1/10 17:32
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public JsonResult<Object> register(AccountRegisterRequest request) {
        return null;
    }
}
