package com.spring.demo.xml.properties;

import lombok.Data;

/**
 * @author HuSen
 * @since 2020/7/10 2:24 下午
 */
@Data
public class BigLongProperties {

    public BigLongProperties() {
        this.sub = new SubBigLongProperties();
    }

    private String name;
    private Integer age;
    private Boolean best;
    private SubBigLongProperties sub;
}
