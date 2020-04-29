package com.gapache.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * 使用自增ID的实体基类
 *
 * @author HuSen
 * create on 2020/4/29 3:20 下午
 */
@Getter
@Setter
@MappedSuperclass
public class BaseEntity<ID extends Serializable> {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private ID id;
}
