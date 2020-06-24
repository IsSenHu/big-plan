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
}
