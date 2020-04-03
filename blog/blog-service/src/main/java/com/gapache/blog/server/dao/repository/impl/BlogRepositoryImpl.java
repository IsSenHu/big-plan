package com.gapache.blog.server.dao.repository.impl;

import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogRepository;
import com.gapache.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;

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
    public IndexResponse index(Blog blog) {
        try {
            XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
            jsonBuilder.startObject();
            {
                jsonBuilder.field("title", blog.getTitle());
                jsonBuilder.field("content", blog.getContent());
                jsonBuilder.field("introduction", blog.getIntroduction());
                jsonBuilder.timeField("publishTime", blog.getPublishTime());
                jsonBuilder.field("category", blog.getCategory());
                jsonBuilder.array("tags", blog.getTags());
                jsonBuilder.field("views", blog.getViews());
            }
            jsonBuilder.endObject();

            IndexRequest request = new IndexRequest()
                    .index("blog")
                    .id(blog.getId())
                    .source(jsonBuilder);


            return client.index(request, RequestOptions.DEFAULT);
        } catch (IOException e) {
            log.error("index error.", e);
            return null;
        }
    }

    @Override
    public SearchResponse archly() {
        SearchRequest request = new SearchRequest("blog");

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.from(0).size(10000);

        String[] includeFields = new String[] {"title", "publishTime"};
        String[] excludeFields = new String[] {"content", "introduction", "category", "tags", "views"};
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
    public Blog get(String id) {
        try {
            GetRequest request = new GetRequest()
                    .index("blog")
                    .id(id);
            GetResponse response = client.get(request, RequestOptions.DEFAULT);
            Map<String, Object> sourceAsMap = response.getSourceAsMap();

            Blog blog = new Blog();
            blog.setId(id);
            blog.setTitle(sourceAsMap.get("title").toString());
            blog.setContent(sourceAsMap.get("content").toString());
            blog.setIntroduction(sourceAsMap.get("introduction").toString());
            blog.setPublishTime(TimeUtils.parse(TimeUtils.Format._3, sourceAsMap.get("publishTime").toString()));
            blog.setCategory(sourceAsMap.get("category").toString());
            blog.setTags(sourceAsMap.get("tags").toString().split(" "));
            blog.setViews(Long.parseLong(sourceAsMap.get("views").toString()));

            return blog;
        } catch (IOException e) {
            log.error("get error.", e);
            return null;
        }
    }
}
