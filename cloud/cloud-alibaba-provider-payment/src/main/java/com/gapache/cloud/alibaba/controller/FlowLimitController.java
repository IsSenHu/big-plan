package com.gapache.cloud.alibaba.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @see com.alibaba.csp.sentinel.slots.block.flow.controller.DefaultController
 * @see com.alibaba.csp.sentinel.slots.block.flow.controller.WarmUpController
 * java -Dserver.port=8080 -Dcsp.sentinel.dashboard.server=localhost:8080
 * -Dproject.name=sentinel-dashboard -jar sentinel-dashboard-1.7.2.jar
 *
 * @author HuSen
 * create on 2020/6/13 11:42
 */
@RestController
public class FlowLimitController {

    @GetMapping("/testA")
    public String testA() {
        return "------testA";
    }

    @GetMapping("/testB")
    public String testB() {
        return "------testB";
    }
}
