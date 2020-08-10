package com.spring.demo.java.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author HuSen
 * @since 2020/8/7 11:04 上午
 */
@Data
@Document(indexName = "cat")
public class Cat {

    @Id
    private String id;

    @Field(type = FieldType.Keyword)
    private String name;

    @Field(type = FieldType.Integer)
    private Integer age;
}
