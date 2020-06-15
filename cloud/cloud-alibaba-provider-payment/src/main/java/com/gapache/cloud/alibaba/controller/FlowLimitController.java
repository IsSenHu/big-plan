package com.gapache.cloud.alibaba.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @see com.alibaba.csp.sentinel.slots.block.flow.controller.DefaultController
 * @see com.alibaba.csp.sentinel.slots.block.flow.controller.WarmUpController
 * @see com.alibaba.csp.sentinel.slots.block.BlockException
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

    @GetMapping("/testD")
    public String testD() {
        int age = 1 / 0;
        return "------TestD";
    }

    @GetMapping("/testHotKey")
    @SentinelResource(value = "testHotKey", blockHandler = "testHotKeyHandler")
    public String testHotKey(@RequestParam(required = false) String p1, @RequestParam(required = false) String p2) {
        // 热点规则
        // 通过资源名配置的规则才走blockHandler
        // 通过url的返回默认值
        return "------testHotKey";
    }

    public String testHotKeyHandler(String p1, String p2, BlockException exception) {
        return "------testHotKeyHandler";
    }
}
