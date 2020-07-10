package com.spring.demo.xml.service;

import lombok.Setter;

/**
 * @author HuSen
 * @since 2020/7/9 6:05 下午
 */
@Setter
public class ClientService {

    private String name;

    private static ClientService clientService = new ClientService();

    public static ClientService createInstance() {
        return clientService;
    }

    public static ClientService createInstance(String name) {
        ClientService clientService = new ClientService();
        clientService.setName(name);
        return clientService;
    }

    public void test() {
        System.out.println("ClientService.test():" + this.name);
    }
}
