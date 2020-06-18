package com.gapache.cloud.seata.order.serivce.impl;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.cloud.seata.order.dao.po.OrderPO;
import com.gapache.cloud.seata.order.dao.repository.OrderRepository;
import com.gapache.cloud.seata.order.feign.AccountFeign;
import com.gapache.cloud.seata.order.serivce.OrderService;
import com.gapache.commons.model.JsonResult;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author HuSen
 * @since 2020/6/17 4:34 下午
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private AccountFeign accountFeign;

    @Resource
    private OrderRepository orderRepository;

    @Override
    @GlobalTransactional(name = "purchase", timeoutMills = 5000, rollbackFor = Exception.class)
    public JsonResult<OrderVO> create(String userId, String commodityCode, int orderCount) {
        int orderMoney = calculate(commodityCode, orderCount);
        accountFeign.debit(userId, orderMoney);

        OrderPO order = new OrderPO();
        order.setUserId(userId);
        order.setOrderBn(UUID.randomUUID().toString().replaceAll("-", ""));
        order.setCommodityCode(commodityCode);
        order.setCount(orderCount);
        order.setMoney(orderMoney);
        orderRepository.save(order);

        OrderVO vo = new OrderVO();
        BeanUtils.copyProperties(order, vo);
        vo.setId(order.getId());
        return JsonResult.of(vo);
    }

    private int calculate(String commodityCode, int orderCount) {
        return orderCount * 20;
    }
}
