package com.gapache.blog.server.dao.repository;

import com.gapache.blog.common.model.dto.BlogQueryDTO;
import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.common.model.dto.BlogSummaryDTO;
import com.gapache.commons.model.IPageRequest;
import org.elasticsearch.action.search.SearchResponse;

import java.util.List;

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

    /**
     * 删除所有的博客
     * @return 是否成功
     */
    boolean deleteAll();

    /**
     * 根据分类查询博客
     *
     * @param category 分类
     * @return 查询结果
     */
    List<Blog> findAllByCategory(String category);

    /**
     * 类似于分类查询博客
     *
     * @param iPageRequest 分页参数
     * @return 查询结果
     */
    List<Blog> find(IPageRequest<BlogQueryDTO> iPageRequest);

    /**
     * 根据标签查询博客
     *
     * @param tag 标签
     * @return 查询结果
     */
    List<Blog> findAllByTag(String tag);

    /**
     * 搜索博客
     *
     * @param queryString 查询字符串
     * @return 博客摘要
     */
    List<BlogSummaryDTO> search(String queryString);

    /**
     * 根据ID查询博客
     *
     * @param id ID
     * @return 博客
     */
    Blog get(String id);

    /**
     * 获取最新的指定数量的博客
     *
     * @param number 指定的数量
     * @return 最新的博客
     */
    List<Blog> getNewest(Integer number);
}
