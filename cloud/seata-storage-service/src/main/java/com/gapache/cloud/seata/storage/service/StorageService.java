package com.gapache.cloud.seata.storage.service;

/**
 * @author HuSen
 * @since 2020/6/17 5:23 下午
 */
public interface StorageService {

    /**
     * 扣除存储数量
     *
     * @param commodityCode 商品编码
     * @param count         数量
     */
    void deduct(String commodityCode, int count);
}
