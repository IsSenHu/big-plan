package com.spring.demo.xml.service.impl;

import com.spring.demo.xml.dao.AccountDao;
import com.spring.demo.xml.dao.ItemDao;
import com.spring.demo.xml.service.PetStoreService;

/**
 * @author HuSen
 * @since 2020/7/7 11:52 下午
 */
public class PetStoreServiceImpl implements PetStoreService {

    private AccountDao accountDao;

    private ItemDao itemDao;

    public void setAccountDao(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    public void setItemDao(ItemDao itemDao) {
        this.itemDao = itemDao;
    }

    @Override
    public void buy(int petId) {
        accountDao.spentMoney(petId);
        itemDao.decrement(petId);
    }

    public static class InnerPetStoreServiceImpl implements PetStoreService {
        @Override
        public void buy(int petId) {
            System.out.println(petId);
        }
    }
}
