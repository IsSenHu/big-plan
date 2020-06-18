package com.gapache.cloud.seata.business.service.impl;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.cloud.seata.business.feign.OrderFeign;
import com.gapache.cloud.seata.business.feign.StorageFeign;
import com.gapache.cloud.seata.business.service.BusinessService;
import com.gapache.commons.model.JsonResult;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 4:22 下午
 */
@Service
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private OrderFeign orderFeign;

    @Resource
    private StorageFeign storageFeign;

    @Override
    @GlobalTransactional(name = "purchase", timeoutMills = 5000, rollbackFor = Exception.class)
    public JsonResult<OrderVO> purchase(String userId, String commodityCode, int orderCount) {
        storageFeign.deduct(commodityCode, orderCount);
        return orderFeign.create(userId, commodityCode, orderCount);
    }
}
