package com.gapache.mybatis.demo.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gapache.mybatis.demo.annotation.Log;
import com.gapache.mybatis.demo.dao.mapper.OrderMapper;
import com.gapache.mybatis.demo.dao.po.OrderPO;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * @since 2020/5/18 9:42 上午
 */
@Slf4j
@Setter
@Service("demoService")
public class DemoService implements InitializingBean {
    private OrderMapper orderMapper;

    @Log
    public void test() {
        IPage<OrderPO> page = new Page<>();
        page.setCurrent(1).setSize(10);
        orderMapper.page(page);
        log.info("test:{}", JSON.toJSONString(page));
    }

    @Override
    public void afterPropertiesSet() {

    }
}
