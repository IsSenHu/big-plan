package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.document.Blog;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;

/**
 * @author HuSen
 * create on 2020/4/3 1:13 下午
 */
public interface BlogRepository {

    /**
     *  为Blog文档建立索引
     *
     * @param blog Blog文档
     * @return 索引建立结果
     */
    IndexResponse index(Blog blog);

    /**
     * 进行归档
     *
     * @return 归档结果
     */
    SearchResponse archly();

    /**
     * 查询博客
     *
     * @param id ID
     * @return 博客
     */
    Blog get(String id);

    /**
     * 删除博客
     *
     * @param id ID
     * @return 操作结果
     */
    boolean delete(String id);
}
