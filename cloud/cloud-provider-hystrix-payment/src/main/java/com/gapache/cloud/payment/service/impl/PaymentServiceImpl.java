package com.gapache.cloud.payment.service.impl;

import com.gapache.cloud.payment.service.PaymentService;
import com.gapache.commons.model.Error;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

/**
 * 服务降级
 * 服务熔断
 *
 * @see com.netflix.hystrix.HystrixCommandProperties
 *
 * @author HuSen
 * @since 2020/6/10 9:53 上午
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public JsonResult<String> randomInteger() {
        return JsonResult.of("线程: " + Thread.currentThread().getName() + " random int: " + RANDOM.nextInt() + " 哈哈");
    }

    /**
     * 这种方式，主要是用于让服务器自己保护自己。
     * 并且能够在业务发生异常的时候，返回一个兜底的结果。
     *
     * 加了这个注解后，此方法会被代理，执行这个方法的线程为Hystrix的线程。
     *
     * @return 随机int
     */
    @Override
    @HystrixCommand(fallbackMethod = "randomIntegerErrorHandler", commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "5000")
    })
    public JsonResult<String> randomIntegerError() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int nextInt = RANDOM.nextInt();
        ThrowUtils.throwIfTrue(nextInt < 0, new Error() {
            @Override
            public Integer getCode() {
                return 233;
            }

            @Override
            public String getError() {
                return "";
            }
        });
        return JsonResult.of("线程: " + Thread.currentThread().getName() + " random int: " + nextInt + " 哈哈");
    }

    public JsonResult<String> randomIntegerErrorHandler() {
        return JsonResult.of("线程: " + Thread.currentThread().getName() + " random int: " + RANDOM.nextInt() + " 呜呜");
    }

    @Override
    @HystrixCommand(fallbackMethod = "randomIntegerCircuitBreakerHandler", commandProperties = {
            // 开启断路器
            @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
            // 请求总数阈值
            @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "10"),
            // 快照时窗口
            @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "10000"),
            // 错误百分比阈值
            @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "60")
    })
    public JsonResult<String> randomIntegerCircuitBreaker(int id) {
        ThrowUtils.throwIfTrue(id < 0, new Error() {
            @Override
            public Integer getCode() {
                return 233;
            }

            @Override
            public String getError() {
                return "";
            }
        });
        return JsonResult.of("线程: " + Thread.currentThread().getName() + " id: " + id + " 哈哈");
    }

    public JsonResult<String> randomIntegerCircuitBreakerHandler(int id) {
        return JsonResult.of("线程: " + Thread.currentThread().getName() + " id: " + id + " 呜呜");
    }
}
