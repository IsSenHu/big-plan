package com.spring.demo.xml.demo;

import lombok.Data;
import org.springframework.beans.factory.DisposableBean;

/**
 * @author HuSen
 * @since 2020/7/11 3:50 下午
 */
@Data
public class Datasource implements DisposableBean {
    private String driverClassName;
    private String url;
    private String username;
    private String password;

    @Override
    public void destroy() {
        System.out.println("Datasource destroy");
    }
}
