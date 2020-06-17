package com.gapache.cloud.seata.account.dao.repository;

import com.gapache.cloud.seata.account.dao.po.AccountPO;
import com.gapache.jpa.BaseJpaRepository;

/**
 * @author HuSen
 * @since 2020/6/17 6:07 下午
 */
public interface AccountRepository extends BaseJpaRepository<AccountPO, Long> {

    /**
     * 查找账户
     *
     * @param userId 用户ID
     * @return 账户信息
     */
    AccountPO findByUserId(String userId);
}
