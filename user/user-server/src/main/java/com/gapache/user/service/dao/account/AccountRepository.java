package com.gapache.user.service.dao.account;

import com.gapache.jpa.BaseJpaRepository;

/**
 * @author HuSen
 * create on 2020/1/10 17:30
 */
public interface AccountRepository extends BaseJpaRepository<AccountPO, Long>, CustomAccountRepository {

    /**
     * 通过手机号查找用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    AccountPO findByPhone(String phone);
}
