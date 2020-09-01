package com.gapache.cloud.money.management.server.service.impl;

import com.gapache.cloud.money.management.common.model.TransactionDTO;
import com.gapache.cloud.money.management.server.dao.entity.TransactionEntity;
import com.gapache.cloud.money.management.server.dao.repository.TransactionRepository;
import com.gapache.cloud.money.management.server.service.TransactionService;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;
import com.gapache.jpa.PageHelper;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.model.AccessCard;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/11 10:22 上午
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void save(List<TransactionDTO> dtoList) {
        AccessCard accessCard = AccessCardHolder.getContext();
        Long userId = accessCard.getUserId();
        for (TransactionDTO dto : dtoList) {
            TransactionEntity entity = transactionRepository.findByTransactionId(dto.getTransactionId());
            if (entity != null) {
                continue;
            }
            entity = new TransactionEntity();
            BeanUtils.copyProperties(dto, entity);
            entity.setUserId(userId);
            transactionRepository.save(entity);
        }
    }

    @Override
    public PageResult<TransactionDTO> page(IPageRequest<TransactionDTO> iPageRequest) {
        Pageable pageable = PageHelper.of(iPageRequest);
        Page<TransactionEntity> page = transactionRepository.findAll(pageable);
        return PageResult.of(page.getTotalElements(), entity -> {
            TransactionDTO dto = new TransactionDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }, page.getContent());
    }
}
