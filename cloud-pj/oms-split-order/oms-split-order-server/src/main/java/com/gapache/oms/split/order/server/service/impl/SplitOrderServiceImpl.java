package com.gapache.oms.split.order.server.service.impl;

import com.gapache.oms.order.base.model.vo.order.OrderBaseVO;
import com.gapache.oms.split.order.server.lua.OrderLuaScript;
import com.gapache.oms.split.order.server.service.SplitOrderService;
import com.gapache.redis.RedisLuaExecutor;
import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void autoSplitOrder(OrderBaseVO order) {
        // 校验
        if (Objects.isNull(order)) {
            return;
        }
        String result = redisLuaExecutor.execute(OrderLuaScript.CHECK_ORDER_UNIQUE, Collections.singletonList(order.getOrderBn()));
        log.debug("订单号去重校验结果:{}", result);
    }
}
