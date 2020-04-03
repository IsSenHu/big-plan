package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.document.Tag;
import com.gapache.blog.server.dao.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/3 5:11 下午
 */
@Slf4j
@Repository
public class TagRepositoryImpl implements TagRepository {

    private final RestHighLevelClient client;

    public TagRepositoryImpl(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public List<Tag> get() {
        SearchRequest request = new SearchRequest("tag");

        try {
            SearchResponse response = client.search(request, RequestOptions.DEFAULT);
            SearchHits searchHits = response.getHits();
            List<Tag> tags = new ArrayList<>(Long.valueOf(searchHits.getTotalHits().value).intValue());
            for (SearchHit hit : searchHits.getHits()) {
                Tag tag = new Tag();
                tags.add(tag);
                Map<String, Object> sourceAsMap = hit.getSourceAsMap();
                tag.setId(hit.getId());
                tag.setName(sourceAsMap.get("name").toString());
                tag.setCount(Integer.parseInt(sourceAsMap.get("count").toString()));
            }

            return tags;
        } catch (IOException e) {
            log.error("get error.", e);
            return new ArrayList<>(0);
        }
    }
}
