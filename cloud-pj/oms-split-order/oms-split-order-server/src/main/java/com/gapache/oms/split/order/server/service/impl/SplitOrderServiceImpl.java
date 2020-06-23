package com.gapache.oms.split.order.server.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.order.base.model.vo.order.OrderBaseVO;
import com.gapache.oms.split.order.server.lua.OrderLuaScript;
import com.gapache.oms.split.order.server.service.SplitOrderService;
import com.gapache.oms.store.location.sdk.feign.AreaFeign;
import com.gapache.oms.store.location.sdk.model.vo.AreaVO;
import com.gapache.oms.store.location.sdk.model.vo.CityVO;
import com.gapache.oms.store.location.sdk.model.vo.ProvinceVO;
import com.gapache.redis.RedisLuaExecutor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
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
    private AreaFeign areaFeign;

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
        if (StringUtils.isNotBlank(result)) {
            return;
        }

        JsonResult<List<ProvinceVO>> province = areaFeign.findAllProvince("四川");
        log.debug("province:{}", province.getData());

        JsonResult<List<CityVO>> city = areaFeign.findAllCity(province.getData().get(0).getCode(), "眉山");
        log.info("city:{}", city.getData());

        JsonResult<List<AreaVO>> allArea = areaFeign.findAllArea(city.getData().get(0).getCode(), "");
        log.info("allArea:{}", allArea.getData());
    }
}
