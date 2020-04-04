package com.gapache.blog.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.gapache.blog.sdk.dubbo.blog.BlogApiService;
import com.gapache.blog.sdk.dubbo.blog.BlogVO;
import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.service.BlogService;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author HuSen
 * create on 2020/4/5 03:33
 */
@Service
@Component
public class BlogApiServiceImpl implements BlogApiService {

    private final BlogService blogService;

    public BlogApiServiceImpl(BlogService blogService) {
        this.blogService = blogService;
    }

    @Override
    public void create(BlogVO blog) {
        Blog document = new Blog();
        document.setId(blog.getId());
        document.setTitle(blog.getTitle());
        document.setIntroduction(blog.getIntroduction());
        document.setContent(new String(blog.getContent(), StandardCharsets.UTF_8));
        document.setPublishTime(blog.getPublishTime());
        document.setCategory(blog.getCategory());
        document.setTags(blog.getTags());
        document.setViews(0L);
        blogService.index(document);
    }
}
