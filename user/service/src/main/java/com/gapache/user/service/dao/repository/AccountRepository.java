package com.gapache.user.service.dao.repository;

import com.gapache.user.service.dao.po.AccountPO;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author HuSen
 * create on 2020/1/10 17:30
 */
public interface AccountRepository extends JpaRepository<AccountPO, Long> {

    AccountPO findByPhone(String phone);
}
