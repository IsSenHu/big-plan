package com.gapache.cloud.seata.account.service;

/**
 * @author HuSen
 * @since 2020/6/17 6:09 下午
 */
public interface AccountService {

    /**
     * 从用户账户中借出
     *
     * @param userId 用户ID
     * @param money  金额
     */
    void debit(String userId, int money);
}
