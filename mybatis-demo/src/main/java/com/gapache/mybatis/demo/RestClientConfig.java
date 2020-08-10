package com.gapache.mybatis.demo;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

/**
 * @author HuSen
 * @since 2020/8/7 10:41 上午
 */
@Configuration
public class RestClientConfig extends AbstractElasticsearchConfiguration {

    private final RestHighLevelClient restHighLevelClient;

    public RestClientConfig(RestHighLevelClient restHighLevelClient) {
        this.restHighLevelClient = restHighLevelClient;
    }

    @Override
    public RestHighLevelClient elasticsearchClient() {
        return this.restHighLevelClient;
    }
}
