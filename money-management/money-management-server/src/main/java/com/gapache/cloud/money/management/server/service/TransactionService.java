package com.gapache.cloud.money.management.server.service;

import com.gapache.cloud.money.management.common.model.TransactionDTO;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/11 10:22 上午
 */
public interface TransactionService {

    /**
     * 保存
     *
     * @param dtoList 交易集合
     */
    void save(List<TransactionDTO> dtoList);

    /**
     * 分页
     *
     * @param iPageRequest 分页参数
     * @return 分页结果
     */
    PageResult<TransactionDTO> page(IPageRequest<TransactionDTO> iPageRequest);
}
