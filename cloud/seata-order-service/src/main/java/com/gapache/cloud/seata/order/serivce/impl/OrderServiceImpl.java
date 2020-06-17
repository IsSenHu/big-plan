package com.gapache.cloud.seata.order.serivce.impl;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.cloud.seata.order.dao.po.OrderPO;
import com.gapache.cloud.seata.order.dao.repository.OrderRepository;
import com.gapache.cloud.seata.order.feign.AccountFeign;
import com.gapache.cloud.seata.order.serivce.OrderService;
import com.gapache.commons.model.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author HuSen
 * @since 2020/6/17 4:34 下午
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Resource
    private AccountFeign accountFeign;

    @Resource
    private OrderRepository orderRepository;

    @Override
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
        return JsonResult.of(vo);
    }

    private int calculate(String commodityCode, int orderCount) {
        return orderCount * 20;
    }
}
