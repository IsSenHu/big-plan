package com.gapache.blog.server.service;

import com.gapache.blog.server.model.vo.ArchiveVO;
import com.gapache.blog.server.model.vo.BlogVO;
import com.gapache.blog.server.model.vo.RankVO;
import com.gapache.blog.server.model.vo.SimpleBlogVO;
import com.gapache.commons.model.JsonResult;

import java.util.List;

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
}
