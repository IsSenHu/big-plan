package com.gapache.cloud.seata.storage.service.impl;

import com.gapache.cloud.seata.storage.dao.po.StoragePO;
import com.gapache.cloud.seata.storage.dao.repository.StorageRepository;
import com.gapache.cloud.seata.storage.error.StorageError;
import com.gapache.cloud.seata.storage.service.StorageService;
import com.gapache.commons.model.ThrowUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 5:23 下午
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageRepository storageRepository;

    @Override
    public void deduct(String commodityCode, int count) {
        StoragePO storage = storageRepository.findByCommodityCode(commodityCode);
        ThrowUtils.throwIfTrue(storage.getCount() < count, StorageError.INVENTORY_BALANCE);
        storage.setCount(storage.getCount() - count);
        storageRepository.save(storage);
    }
}
