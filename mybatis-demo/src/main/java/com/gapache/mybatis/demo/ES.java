package com.gapache.mybatis.demo;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

/**
 * @author HuSen
 * @since 2020/8/7 10:16 上午
 */
@Configuration
public class ES {

    @Bean
    public RestClientBuilder elasticsearchRestClientBuilder() {
        HttpHost[] hosts = Stream.of("dev.51ishare.com:9200").map(HttpHost::create).toArray(HttpHost[]::new);
        RestClientBuilder builder = RestClient.builder(hosts);
        builder.setRequestConfigCallback((requestConfigBuilder) -> {
            requestConfigBuilder.setConnectTimeout(30);
            requestConfigBuilder.setSocketTimeout(30);
            return requestConfigBuilder;
        });
        return builder;
    }

    @Bean
    public RestHighLevelClient elasticsearchRestHighLevelClient(RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }

    @Bean
    public RestClient elasticsearchRestClient(RestClientBuilder builder,
                                       ObjectProvider<RestHighLevelClient> restHighLevelClient) {
        RestHighLevelClient client = restHighLevelClient.getIfUnique();
        if (client != null) {
            return client.getLowLevelClient();
        }
        return builder.build();
    }

    @Bean
    public RestClient elasticsearchRestClient(RestClientBuilder builder) {
        return builder.build();
    }
}
