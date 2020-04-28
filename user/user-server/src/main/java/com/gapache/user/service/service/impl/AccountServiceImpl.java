package com.gapache.user.service.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.uid.UidGenerator;
import com.gapache.user.service.dao.account.AccountPO;
import com.gapache.user.service.dao.account.AccountRepository;
import com.gapache.user.service.model.AccountError;
import com.gapache.user.service.model.AccountRegisterRequest;
import com.gapache.user.service.model.CheckAccountRequest;
import com.gapache.user.service.model.vo.AccountVO;
import com.gapache.user.service.service.AccountService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * create on 2020/1/10 17:32
 */
@Service
public class AccountServiceImpl implements AccountService {

    private final UidGenerator uidGenerator;
    private final AccountRepository accountRepository;

    public AccountServiceImpl(UidGenerator uidGenerator, AccountRepository accountRepository) {
        this.uidGenerator = uidGenerator;
        this.accountRepository = accountRepository;
        this.accountRepository.test();
    }

    @Override
    public JsonResult<Object> register(AccountRegisterRequest request) {
        AccountPO po = new AccountPO();
        long uid = uidGenerator.getUID();
        po.setId(uid);
        po.setPhone(request.getPhone());
        po.setPassword(request.getPassword());
        po.setNickName(String.valueOf(uid));
        po.setCreateTime(LocalDateTime.now());
        accountRepository.save(po);
        return JsonResult.success();
    }

    @Override
    public JsonResult<AccountVO> check(CheckAccountRequest request) {
        AccountPO account = accountRepository.findByPhone(request.getPhone());
        ThrowUtils.throwIfTrue(account == null ||
                !StringUtils.equals(account.getPassword(), request.getPassword()), AccountError.PHONE_OR_PASSWORD_WRONG);

        AccountVO vo = new AccountVO();
        BeanUtils.copyProperties(account, vo);
        return JsonResult.of(vo);
    }
}
