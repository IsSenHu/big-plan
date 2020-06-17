package com.gapache.cloud.seata.business.controller;

import com.gapache.cloud.sdk.OrderVO;
import com.gapache.cloud.seata.business.service.BusinessService;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/17 6:14 下午
 */
@RestController
@RequestMapping("/api/business")
public class BusinessController {

    @Resource
    private BusinessService businessService;

    @GetMapping("/purchase")
    public JsonResult<OrderVO> purchase(String userId, String commodityCode, int orderCount) {
        return businessService.purchase(userId, commodityCode, orderCount);
    }
}
