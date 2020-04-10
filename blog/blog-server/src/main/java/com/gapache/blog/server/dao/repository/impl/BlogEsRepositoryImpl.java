package com.gapache.blog.server.dao.repository.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogEsRepository;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.CountRequest;
import org.elasticsearch.client.core.CountResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/4/3 1:13 下午
 */
@Slf4j
@Repository
public class BlogEsRepositoryImpl implements BlogEsRepository {

    private static final String INDEX = "blog";

    private final RestHighLevelClient client;

    public BlogEsRepositoryImpl(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public void index(Blog blog) {
        try {
            XContentBuilder jsonBuilder = toJsonBuilder(blog);
            IndexRequest request = new IndexRequest()
                    .index(INDEX)
                    .id(blog.getId())
                    .source(jsonBuilder);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);
            if (log.isDebugEnabled()) {
                log.debug("index:{}", response);
            }
        } catch (IOException e) {
            log.error("index error.", e);
        }
    }

    @Override
    public SearchResponse archly() {
        SearchRequest request = new SearchRequest(INDEX);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0).size(10000);

        String[] includeFields = new String[] {"title", "publishTime"};
        String[] excludeFields = new String[] {"content", "introduction", "category", "tags"};
        sourceBuilder.fetchSource(includeFields, excludeFields);

        try {
            request.source(sourceBuilder);
            return client.search(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("archly error.", e);
            return null;
        }
    }

    @Override
    public void delete(String id) {
        DeleteRequest request = new DeleteRequest()
                .index(INDEX)
                .id(id);

        try {
            client.delete(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("delete error.", e);
        }
    }

    @Override
    public void update(Blog blog) {
        try {
            XContentBuilder jsonBuilder = toJsonBuilder(blog);

            UpdateRequest request = new UpdateRequest()
                    .index(INDEX)
                    .id(blog.getId())
                    .doc(jsonBuilder);

            UpdateResponse response = client.update(request, RequestOptions.DEFAULT);
            if (log.isDebugEnabled()) {
                log.debug("update:{}", response);
            }
        } catch (IOException e) {
            log.error("update error.", e);
        }
    }

    @Override
    public boolean deleteAll() {
        try {
            DeleteIndexRequest request = new DeleteIndexRequest(INDEX);
            request.timeout(TimeValue.timeValueMinutes(2));
            request.timeout("2m");
            request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
            request.masterNodeTimeout("1m");
            request.indicesOptions(IndicesOptions.lenientExpandOpen());
            client.indices().delete(request, RequestOptions.DEFAULT);
            return true;
        } catch (IOException e) {
            log.error("delete all error.", e);
            return false;
        }
    }

    @Override
    public List<Blog> findAllByCategory(String category) {
        return findAll(QueryBuilders.matchQuery("category", category));
    }

    @Override
    public List<Blog> findAllByTag(String tag) {
        return findAll(QueryBuilders.matchQuery("tags", tag));
    }

    private List<Blog> findAll(QueryBuilder queryBuilder) {
        try {
            CountRequest countRequest = new CountRequest(INDEX);
            countRequest.query(queryBuilder);
            CountResponse countResponse = client.count(countRequest, RequestOptions.DEFAULT);
            long count = countResponse.getCount();

            SearchRequest searchRequest = new SearchRequest(INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder)
                    .from(0)
                    .size((int) count);
            String[] includeFields = new String[] {"id", "title", "publishTime", "introduction"};
            String[] excludeFields = new String[] {"content", "category", "tags"};
            searchSourceBuilder.fetchSource(includeFields, excludeFields);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            SearchHit[] hits = searchResponse.getHits().getHits();
            return Arrays.stream(hits)
                    .map(hit -> {
                        Blog blog = JSON.parseObject(hit.getSourceAsString(), Blog.class);
                        blog.setId(hit.getId());
                        return blog;
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("find all by category error.", e);
            return Lists.newArrayList();
        }
    }

    private XContentBuilder toJsonBuilder(Blog blog) throws IOException {
        XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
        jsonBuilder.startObject();
        {
            jsonBuilder.field("title", blog.getTitle());
            jsonBuilder.field("content", blog.getContent());
            jsonBuilder.field("introduction", blog.getIntroduction());
            jsonBuilder.timeField("publishTime", blog.getPublishTime());
            jsonBuilder.field("category", blog.getCategory());
            jsonBuilder.array("tags", blog.getTags());
        }
        jsonBuilder.endObject();
        return jsonBuilder;
    }
}
