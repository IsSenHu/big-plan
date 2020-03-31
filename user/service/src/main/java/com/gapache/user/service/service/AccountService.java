package com.gapache.user.service.service;

import com.gapache.commons.model.JsonResult;
import com.gapache.user.service.model.AccountRegisterRequest;
import com.gapache.user.service.model.CheckAccountRequest;
import com.gapache.user.service.model.vo.AccountVO;

/**
 * @author HuSen
 * create on 2020/1/10 17:31
 */
public interface AccountService {

    JsonResult<Object> register(AccountRegisterRequest request);

    JsonResult<AccountVO> check(CheckAccountRequest request);
}
