package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.document.Blog;
import org.elasticsearch.action.search.SearchResponse;

/**
 * @author HuSen
 * create on 2020/4/3 1:13 下午
 */
public interface BlogEsRepository {

    /**
     * 为Blog文档建立索引
     *
     * @param blog Blog文档
     */
    void index(Blog blog);

    /**
     * 进行归档
     *
     * @return 归档结果
     */
    SearchResponse archly();

    /**
     * 删除博客
     *
     * @param id ID
     */
    void delete(String id);

    /**
     * 更新博客
     *
     * @param blog 博客
     */
    void update(Blog blog);
}
