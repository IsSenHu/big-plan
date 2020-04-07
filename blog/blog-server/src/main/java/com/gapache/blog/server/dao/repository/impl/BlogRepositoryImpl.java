package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;

/**
 * @author HuSen
 * create on 2020/4/3 1:13 下午
 */
@Slf4j
@Repository
public class BlogRepositoryImpl implements BlogRepository {

    private final RestHighLevelClient client;

    public BlogRepositoryImpl(RestHighLevelClient client) {
        this.client = client;
    }

    @Override
    public void index(Blog blog) {
        try {
            XContentBuilder jsonBuilder = toJsonBuilder(blog);
            IndexRequest request = new IndexRequest()
                    .index("blog")
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
        SearchRequest request = new SearchRequest("blog");

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
    public boolean delete(String id) {
        DeleteRequest request = new DeleteRequest()
                .index("blog")
                .id(id);

        try {
            DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
            return response.status().equals(RestStatus.OK);
        } catch (IOException e) {
            log.error("delete error.", e);
            return false;
        }
    }

    @Override
    public void update(Blog blog) {
        try {
            XContentBuilder jsonBuilder = toJsonBuilder(blog);

            UpdateRequest request = new UpdateRequest()
                    .index("blog")
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
