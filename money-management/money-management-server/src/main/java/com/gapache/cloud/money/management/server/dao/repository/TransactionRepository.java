package com.gapache.cloud.money.management.server.dao.repository;

import com.gapache.cloud.money.management.server.dao.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author HuSen
 * @since 2020/8/11 10:22 上午
 */
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    /**
     * 根据交易ID查询交易
     *
     * @param transactionId 交易ID
     * @return 交易
     */
    TransactionEntity findByTransactionId(String transactionId);
}
