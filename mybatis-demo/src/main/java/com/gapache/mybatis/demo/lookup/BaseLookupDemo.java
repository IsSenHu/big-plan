package com.gapache.mybatis.demo.lookup;

import com.gapache.mybatis.demo.service.AaService;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * @since 2020/5/27 6:05 下午
 */
@Component
public class BaseLookupDemo {

    public void process() {
        AaService aaService = createAaService();
        System.out.println(aaService);
        aaService.test();
    }

    /**
     *  这个方法会被cglib使用动态代理生成子类覆盖，然后返回AaService的实例
     *  适合用于AaService是原型的情况
     *  官方写的例子用的是抽象类和抽象方法，其实写成这样也可以。
     *
     * @return AaService原型单例
     */
    @Lookup
    protected AaService createAaService() {
        return null;
    }
}
