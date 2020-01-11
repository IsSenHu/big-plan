package com.gapache.user.service.service;

import com.gapache.commons.model.JsonResult;
import com.gapache.user.service.model.AccountRegisterRequest;

/**
 * @author HuSen
 * create on 2020/1/10 17:31
 */
public interface AccountService {

    JsonResult<Object> register(AccountRegisterRequest request);
}
