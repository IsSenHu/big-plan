package com.gapache.mybatis.demo.dao.mapper;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gapache.mybatis.demo.DemoApplication;
import com.gapache.mybatis.demo.dao.po.OrderItemPO;
import com.gapache.mybatis.demo.dao.po.OrderJPO;
import com.gapache.mybatis.demo.dao.po.OrderPO;
import com.gapache.mybatis.demo.dao.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

/**
 * @author HuSen
 * create on 2020/5/13 9:51 上午
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DemoApplication.class)
public class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void insert() {
        OrderPO order = new OrderPO();
        order.setOrderBn(UUID.randomUUID().toString().replaceAll("-", ""));
        Assert.assertEquals(orderMapper.insert(order), 1);
        System.out.println(JSON.toJSONString(order, true));
    }

    @Test
    public void insertItem() {
        String orderBn = "21f726ca46dc41fb9d4a2af3218dd83f";
        OrderItemPO item = new OrderItemPO();
        item.setOrderBn(orderBn);
        item.setPrice(2.2);
        Assert.assertEquals(orderItemMapper.insert(item), 1);
        System.out.println(JSON.toJSONString(item, true));

        item = new OrderItemPO();
        item.setOrderBn(orderBn);
        item.setPrice(8.8);
        Assert.assertEquals(orderItemMapper.insert(item), 1);
        System.out.println(JSON.toJSONString(item, true));
    }

    @Test
    public void page() {
        IPage<OrderPO> page = new Page<>();
        page.setCurrent(1);
        page.setSize(2);
        orderMapper.page(page);
        System.out.println(JSON.toJSONString(page, true));
    }

    @Test
    public void jpaPage() {
        PageRequest request = PageRequest.of(0, 1);
        org.springframework.data.domain.Page<OrderJPO> all = orderRepository.findAll(request);
        System.out.println(JSON.toJSONString(all, true));
    }

    @Test
    public void jpaUseMybatis() {
        IPage<OrderPO> page = new Page<>();
        page.setCurrent(1);
        page.setSize(2);
        orderRepository.selectPage(page);
        System.out.println(JSON.toJSONString(page, true));
    }
}