package com.gapache.cloud.seata.storage.service.impl;

import com.gapache.cloud.seata.storage.dao.po.StoragePO;
import com.gapache.cloud.seata.storage.dao.repository.StorageRepository;
import com.gapache.cloud.seata.storage.service.StorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 5:23 下午
 */
@Service
public class StorageServiceImpl implements StorageService {

    @Resource
    private StorageRepository storageRepository;

    @Override
    public void deduct(String commodityCode, int count) {
        StoragePO storage = storageRepository.findByCommodityCode(commodityCode);
        storage.setCount(storage.getCount() - count);
        storageRepository.save(storage);
    }
}
