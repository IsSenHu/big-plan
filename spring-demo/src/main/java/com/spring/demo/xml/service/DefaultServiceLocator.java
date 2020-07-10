package com.spring.demo.xml.service;

/**
 * @author HuSen
 * @since 2020/7/9 6:12 下午
 */
public class DefaultServiceLocator {

    private static ClientService clientService = new ClientService();

    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
