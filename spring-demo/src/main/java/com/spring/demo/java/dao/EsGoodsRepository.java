package com.spring.demo.java.dao;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author HuSen
 * @since 2020/8/21 6:21 下午
 */
public interface EsGoodsRepository extends ElasticsearchRepository<EsGoods, Integer> {
}
