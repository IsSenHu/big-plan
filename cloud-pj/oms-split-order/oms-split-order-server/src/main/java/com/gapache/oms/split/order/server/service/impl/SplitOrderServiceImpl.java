package com.gapache.oms.split.order.server.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.order.base.model.vo.order.ConsigneeVO;
import com.gapache.oms.order.base.model.vo.order.OrderBaseVO;
import com.gapache.oms.split.order.server.lua.OrderLuaScript;
import com.gapache.oms.split.order.server.service.SplitOrderService;
import com.gapache.oms.store.location.sdk.feign.StoreLocationFeign;
import com.gapache.oms.store.location.sdk.model.vo.StoreVO;
import com.gapache.redis.RedisLuaExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Objects;

/**
 * @author HuSen
 * @since 2020/6/22 4:53 下午
 */
@Slf4j
@Service
public class SplitOrderServiceImpl implements SplitOrderService {

    @Resource
    private RedisLuaExecutor redisLuaExecutor;

    @Resource
    private StoreLocationFeign storeLocationFeign;

    @Override
    public void autoSplitOrder(OrderBaseVO order) {
        // 校验
        if (Objects.isNull(order)) {
            return;
        }
        // 订单号去重校验
        String result = redisLuaExecutor.execute(OrderLuaScript.CHECK_ORDER_UNIQUE, Collections.singletonList(order.getOrderBn()));
        if (log.isDebugEnabled()) {
            log.debug("订单号去重校验结果:{}", result);
        }
        if (StringUtils.equalsIgnoreCase(result, Boolean.FALSE.toString())) {
            return;
        }

        StoreVO store;
        // 如果订单没有指定门店编码，则根据地址进行自动分单
        if (StringUtils.isBlank(order.getStoreCode())) {
            ConsigneeVO consignee = order.getConsignee();
            JsonResult<StoreVO> findResult = storeLocationFeign.findClosestDistanceByAddress(consignee.getAreaCity(), consignee.getAddr());
            store = findResult.getData();
        } else {
            JsonResult<StoreVO> findResult = storeLocationFeign.findStore(order.getStoreCode());
            store = findResult.getData();
        }
        // 如果没有找到匹配的门店则让订单进行人工手动分单
        if (store == null) {

        }
    }
}
