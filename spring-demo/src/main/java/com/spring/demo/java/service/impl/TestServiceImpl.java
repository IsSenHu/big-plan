package com.spring.demo.java.service.impl;

import com.spring.demo.java.annotation.TraceMethodInvoke;
import com.spring.demo.java.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * @author HuSen
 * @since 2020/7/7 5:51 下午
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Override
    @TraceMethodInvoke
    public void test() {
        // 如果在被代理类的内部调用调用自己加了切面的方法，切面将不会生效
        // 因为spring在实现aop的时候将会同时存在代理对象和目标对象，具体可以了解cglib实现代理的原理
        // 只有在外部通过代理对象来调用被代理的方法，aop才会生效
        // 在内部自己调用自己是通过目标对象来调用，这时候调用的只是没有织入增强的原来的方法
        // 要在内部调用自己的方法也能使aop生效很简单，就是通过AopContext.currentProxy()获取到当前的代理对象
        // 然后通过代理对象来调用这个方法
        // 注意@@@@@@@@@@
        // @EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
        // exposeProxy属性必须设置为true，这样才能暴露出代理对象，然后使用AopContext来进行获取
        TestService proxy = (TestService) AopContext.currentProxy();
        log.info("this:{}, proxy:{}, log:{}", this, proxy, log.getClass());
        proxy.testInnerAspectJ();
        // 也可以将代理对象注入进来，然后使用代理对象来调用这个方法。
    }

    @Override
    @TraceMethodInvoke
    public void testInnerAspectJ() {

    }
}
