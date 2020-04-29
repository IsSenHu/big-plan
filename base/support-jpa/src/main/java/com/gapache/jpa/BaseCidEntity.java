package com.gapache.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * @author HuSen
 * create on 2020/4/29 3:26 下午
 */
@Getter
@Setter
@MappedSuperclass
public class BaseCidEntity<ID extends Serializable> {

    @Id
    private ID id;
}
