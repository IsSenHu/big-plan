package com.gapache.oms.store.location.server.service;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.StoreVO;

/**
 * @author HuSen
 * @since 2020/6/24 11:36 上午
 */
public interface StoreService {

    /**
     * 创建门店
     *
     * @param store 门店数据
     * @return 创建结果
     */
    JsonResult<StoreVO> create(StoreVO store);

    /**
     * 根据提供的地址查询距离最近或者最符合发货条件的门店
     *
     * @param city    市
     * @param address 详细地址
     * @return 匹配到的门店
     */
    JsonResult<StoreVO> findClosestDistanceByAddress(String city, String address);
}
