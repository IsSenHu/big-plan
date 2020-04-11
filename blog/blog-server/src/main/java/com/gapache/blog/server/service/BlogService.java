package com.gapache.blog.server.service;

import com.gapache.blog.server.model.vo.ArchiveVO;
import com.gapache.blog.server.model.vo.BlogSummaryVO;
import com.gapache.blog.server.model.vo.BlogVO;
import com.gapache.blog.server.model.vo.RankVO;
import com.gapache.blog.sdk.dubbo.blog.SimpleBlogVO;
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
    JsonResult<ArchiveVO> archive();

    /**
     * 查询博客
     *
     * @param id ID
     * @return 博客
     */
    JsonResult<BlogVO> get(String id);

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
     * @param number {number}
     * @return 指定前 {number} 的博客排行
     */
    JsonResult<List<RankVO<SimpleBlogVO>>> top(Integer number);

    /**
     * 根据id查询博客
     *
     * @param ids      id集合
     * @param viewsMap 阅读数map
     * @return 博客集合
     */
    List<SimpleBlogVO> findAllByIds(Collection<String> ids, Map<String, Integer> viewsMap);

    /**
     * 根据分类查询博客
     *
     * @param category 分类
     * @return 查询结果
     */
    JsonResult<List<SimpleBlogVO>> findAllByCategory(String category);

    /**
     * 根据标签查询博客
     *
     * @param tag 标签
     * @return 查询结果
     */
    JsonResult<List<SimpleBlogVO>> findAllByTags(String tag);

    /**
     * 搜索博客
     *
     * @param queryString 查询字符串
     * @return 博客摘要
     */
    JsonResult<List<BlogSummaryVO>> search(String queryString);
}
