package com.gapache.mybatis.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * @since 2020/5/27 10:56 上午
 */
@Service
public class BbService {

    @Autowired
    private AaService aaService;

    public void test() {
        System.out.println("B");
        System.out.println(aaService);
    }
}
