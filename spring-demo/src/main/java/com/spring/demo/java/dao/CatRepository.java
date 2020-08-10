package com.spring.demo.java.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author HuSen
 * @since 2020/8/7 12:12 下午
 */
public interface CatRepository extends ElasticsearchRepository<Cat, String> {

    Cat findByName(String name);
}
