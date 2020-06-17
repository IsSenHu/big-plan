package com.gapache.cloud.seata.order.controller;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.cloud.seata.order.serivce.OrderService;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 6:18 下午
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @PostMapping("/create")
    public JsonResult<OrderVO> create(String userId, String commodityCode, int orderCount) {
        return orderService.create(userId, commodityCode, orderCount);
    }
}
