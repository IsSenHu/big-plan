package com.gapache.mybatis.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

/**
 * 1.两个循环依赖的类，都使用构造器注入是无法完成循环依赖的。
 * 2.两个原型不能循环依赖，会被spring抛出异常。
 * 3.一个原型和一个单例可以循环依赖
 *
 * 4.单例依赖原型，则依赖的原型是不变的，因为在注入属性的时候getBean()生成了一个设置了就不会变
 * 5.原型：如果在上下文初始化的时候不需要这个原型的实例，则不会生成这个原型的实例。
 * 6.原型的实例是每次getBean()的时候都会生成一个新的独立的实例。
 *
 * @author HuSen
 * @since 2020/5/27 10:56 上午
 */
@Service
@Scope("prototype")
public class AaService {

    @Autowired
    private BbService bbService;

    public void test() {
        System.out.println("A");
        System.out.println(bbService);
    }
}
