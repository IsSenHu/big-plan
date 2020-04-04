package com.gapache.blog.sdk.dubbo.blog;

/**
 * @author HuSen
 * create on 2020/4/5 03:30
 */
public interface BlogApiService {

    /**
     * 创建博客
     *
     * @param blog 博客
     */
    void create(BlogVO blog);
}
