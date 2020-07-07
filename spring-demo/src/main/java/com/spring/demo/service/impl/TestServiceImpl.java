package com.spring.demo.service.impl;

import com.spring.demo.service.TestService;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * @since 2020/7/7 5:51 下午
 */
@Service
public class TestServiceImpl implements TestService {

    public void test() {
        System.out.println("TestServiceImpl.test()");
    }
}
