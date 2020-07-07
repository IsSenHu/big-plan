package com.spring.demo.xml.dao;

/**
 * @author HuSen
 * @since 2020/7/7 11:54 下午
 */
public class ItemDao {

    public void decrement(int petId) {
        System.out.println("宠物：" + petId + " 库存数量减1");
    }
}
