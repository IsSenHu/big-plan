package com.gapache.cloud.payment.controller;

import com.gapache.cloud.payment.service.PaymentService;
import com.gapache.cloud.sdk.PaymentVO;
import com.gapache.commons.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/6/2 5:13 下午
 */
@Slf4j
@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    private final DiscoveryClient discoveryClient;

    public PaymentController(PaymentService paymentService, DiscoveryClient discoveryClient) {
        this.paymentService = paymentService;
        this.discoveryClient = discoveryClient;
    }

    @PostMapping
    public JsonResult<Long> create(@RequestBody PaymentVO payment) {
        return JsonResult.of(paymentService.create(payment));
    }

    @GetMapping("/{id}")
    public JsonResult<PaymentVO> get(@PathVariable("id") Long id) {
        return JsonResult.of(paymentService.findById(id));
    }

    @GetMapping("/discovery")
    public Object discovery() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            log.info("******:{}", service);
        }

        List<ServiceInstance> instances = discoveryClient.getInstances("CLOUD-PAYMENT-SERVICE");
        for (ServiceInstance instance : instances) {
            log.info("serviceId:{}, host:{}, port:{}, uri:{}", instance.getServiceId(), instance.getHost(), instance.getPort(), instance.getUri());
        }

        return discoveryClient;
    }
}
