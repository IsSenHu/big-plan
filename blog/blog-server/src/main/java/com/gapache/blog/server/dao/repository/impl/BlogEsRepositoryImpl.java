package com.gapache.blog.server.dao.repository.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.blog.common.model.dto.BlogQueryDTO;
import com.gapache.blog.common.model.dto.BlogSummaryDTO;
import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogEsRepository;
import com.gapache.commons.model.IPageRequest;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
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
        } catch (Exception e) {
            log.error("index error.", e);
        }
    }

    @Override
    public SearchResponse archly() {
        SearchRequest request = new SearchRequest(INDEX);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0).size(10000);

        String[] includeFields = new String[]{"title", "publishTime"};
        String[] excludeFields = new String[]{"content", "introduction", "category", "tags"};
        sourceBuilder.fetchSource(includeFields, excludeFields);

        try {
            request.source(sourceBuilder);
            return client.search(request, RequestOptions.DEFAULT);
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
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
        } catch (Exception e) {
            log.error("delete all error.", e);
            return false;
        }
    }

    @Override
    public List<Blog> findAllByCategory(String category) {
        return findAll(QueryBuilders.matchQuery("category", category), 0, 10);
    }

    @Override
    public List<Blog> findAllByTag(String tag) {
        return findAll(QueryBuilders.matchQuery("tags", tag), 0, 10);
    }

    @Override
    public List<Blog> find(IPageRequest<BlogQueryDTO> iPageRequest) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BlogQueryDTO customParams = iPageRequest.getCustomParams();
        String tag = customParams.getTag();
        String category = customParams.getCategory();
        if (StringUtils.isNotBlank(tag)) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("tags", tag));
        }
        if (StringUtils.isNotBlank(category)) {
            boolQueryBuilder.must(QueryBuilders.termQuery("category", category));
        }
        return findAll(boolQueryBuilder, iPageRequest.getPage(), iPageRequest.getNumber());
    }

    @Override
    public List<BlogSummaryDTO> search(String queryString) {
        try {
            String[] includeFields = new String[]{"id", "title", "content", "introduction"};
            String[] excludeFields = new String[]{"publishTime", "category", "tags"};
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.fetchSource(includeFields, excludeFields);
            if (StringUtils.isNotBlank(queryString)) {
                // 模糊匹配标题，介绍以及内容
                BoolQueryBuilder queryBuilder = QueryBuilders
                        .boolQuery()
                        .should(QueryBuilders.matchQuery("title", queryString))
                        .should(QueryBuilders.matchQuery("introduction", queryString))
                        .should(QueryBuilders.matchQuery("content", queryString));
                searchSourceBuilder.query(queryBuilder);
            } else {
                searchSourceBuilder.size(5);
            }

            SearchRequest request = new SearchRequest()
                    .indices(INDEX)
                    .source(searchSourceBuilder);
            SearchResponse search = client.search(request, RequestOptions.DEFAULT);

            List<BlogSummaryDTO> result = new ArrayList<>();
            for (SearchHit hit : search.getHits()) {
                Blog blog = JSON.parseObject(hit.getSourceAsString(), Blog.class);
                BlogSummaryDTO summary = new BlogSummaryDTO();
                summary.setId(hit.getId());
                summary.setSummary(blog.getTitle());
                result.add(summary);
            }
            return result;
        } catch (Exception e) {
            log.error("search error.", e);
            return Lists.newArrayList();
        }
    }

    @Override
    public Blog get(String id) {
        try {
            GetRequest request = new GetRequest()
                    .index(INDEX)
                    .id(id);
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            return JSON.parseObject(response.getSourceAsString(), Blog.class);
        } catch (Exception e) {
            log.error("get error.", e);
            return null;
        }
    }

    @Override
    public List<Blog> getNewest(Integer number) {
        return findAll(null, 0, number);
    }

    private List<Blog> findAll(QueryBuilder queryBuilder, Integer from, Integer number) {
        try {
            SearchRequest searchRequest = new SearchRequest(INDEX);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            if (queryBuilder != null) {
                searchSourceBuilder.query(queryBuilder);
            }
            searchSourceBuilder
                    .sort("publishTime", SortOrder.DESC)
                    .from(from)
                    .size(number);
            String[] includeFields = new String[]{"id", "title", "publishTime", "introduction"};
            String[] excludeFields = new String[]{"content", "category", "tags"};
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
        } catch (Exception e) {
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
