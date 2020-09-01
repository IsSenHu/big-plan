package com.gapache.blog.server.service;

import com.gapache.blog.common.model.dto.*;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author HuSen
 * create on 2020/4/3 1:14 下午
 */
public interface BlogService {

    /**
     * 进行归档
     *
     * @return 归档结果
     */
    JsonResult<ArchiveDTO> archive();

    /**
     * 查询博客
     *
     * @param id ID
     * @return 博客
     */
    JsonResult<FullBlogDTO> get(String id);

    /**
     * 增加阅读数
     *
     * @param id 博客ID
     * @return justSuccess
     */
    JsonResult<Object> views(String id);

    /**
     * 获取指定前 {number} 的博客排行
     *
     * @param from   {from}
     * @param number {number}
     * @return 指定从 {from} 开始的前 {number} 的博客排行
     */
    JsonResult<List<RankDTO<SimpleBlogDTO>>> top(Integer from, Integer number);

    /**
     * 类似于分类查询博客
     *
     * @param iPageRequest 分页参数
     * @return 查询结果
     */
    JsonResult<List<RankDTO<SimpleBlogDTO>>> find(IPageRequest<BlogQueryDTO> iPageRequest);

    /**
     * 根据id查询博客
     *
     * @param ids      id集合
     * @param viewsMap 阅读数map
     * @return 博客集合
     */
    List<SimpleBlogDTO> findAllByIds(Collection<String> ids, Map<String, Integer> viewsMap);

    /**
     * 根据分类查询博客
     *
     * @param category 分类
     * @return 查询结果
     */
    JsonResult<List<SimpleBlogDTO>> findAllByCategory(String category);

    /**
     * 根据标签查询博客
     *
     * @param tag 标签
     * @return 查询结果
     */
    JsonResult<List<SimpleBlogDTO>> findAllByTags(String tag);

    /**
     * 搜索博客
     *
     * @param queryString 查询字符串
     * @return 博客摘要
     */
    JsonResult<List<BlogSummaryDTO>> search(String queryString);

    /**
     * 获取最新的指定数量的博客
     *
     * @param number 指定的数量
     * @return 最新的博客
     */
    JsonResult<List<SimpleBlogDTO>> getNewest(Integer number);
}
