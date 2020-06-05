package com.gapache.cloud.order.controller;

import com.gapache.cloud.sdk.PaymentVO;
import com.gapache.commons.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author HuSen
 * create on 2020/6/3 00:10
 */
@Slf4j
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private static final String PAYMENT = "http://CLOUD-PAYMENT-SERVICE";

    private final RestTemplate restTemplate;

    private final DiscoveryClient discoveryClient;

    private final AtomicInteger invokeTimes = new AtomicInteger(0);

    public OrderController(RestTemplate restTemplate, DiscoveryClient discoveryClient) {
        this.restTemplate = restTemplate;
        this.discoveryClient = discoveryClient;
    }

    @GetMapping("/payment/create")
    public JsonResult create(PaymentVO payment) {
        return restTemplate.postForEntity(PAYMENT.concat("/api/payment"), payment, JsonResult.class).getBody();
    }

    @GetMapping("/payment/get/{id}")
    public JsonResult get(@PathVariable Long id) {
        return restTemplate.getForEntity(PAYMENT.concat("/api/payment/".concat(id.toString())), JsonResult.class).getBody();
    }

    @GetMapping("/testLoadBalance")
    public JsonResult<Integer> testLoadBalance() {
        String server = getServer();
        log.info(server);
        Integer body = restTemplate.getForEntity(server.concat("/api/payment/getPort"), Integer.class).getBody();
        return JsonResult.of(body);
    }

    private String getServer() {
        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        int current;
        int next;
        do {
            current = invokeTimes.get();
            next = current == Integer.MAX_VALUE ? 0 : current + 1;
        } while (!invokeTimes.compareAndSet(current, next));

        ServiceInstance serviceInstance = instances.get(next % instances.size());
        return serviceInstance.getUri().toString();
    }
}
