package com.gapache.cloud.alibaba.controller;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.gapache.cloud.alibaba.feign.PaymentFeign;
import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/12 3:08 下午
 */
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final String PAYMENT = "http://nacos-payment-provider";

    @Resource
    private RestTemplate restTemplate;

    @Resource
    private PaymentFeign paymentFeign;

    /**
     * fallback管运行异常
     * blockHandler管配置违规
     * 若blockHandler和fallback都进行了配置，则违规配置而抛出BlockException时只会进入blockHandler处理逻辑
     * 没有违规则进入fallback处理逻辑。
     *
     * @param id id
     * @return result
     */
    @GetMapping("/getPort/{id}")
//    @SentinelResource(value = "fallback", fallback = "handlerFallback")
//    @SentinelResource(value = "fallback", fallback = "handlerFallback", blockHandler = "blockHandler")
    public JsonResult<String> getPort(@PathVariable("id") int id) {
        if (id == 4) {
            throw new IllegalArgumentException("非法参数异常...");
        }
        return paymentFeign.getPort();
    }

    public JsonResult<String> handlerFallback(int id) {
        return JsonResult.of("handlerFallback");
    }

    public JsonResult<String> blockHandler(int id, BlockException exception) {
        return JsonResult.of("blockHandler");
    }
}
