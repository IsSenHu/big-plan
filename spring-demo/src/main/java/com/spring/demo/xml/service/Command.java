package com.spring.demo.xml.service;

import lombok.Data;

/**
 * @author HuSen
 * @since 2020/7/10 4:00 下午
 */
@Data
public class Command {

    private String state;

    public String execute() {
        return this.hashCode() + ":" + state;
    }
}
