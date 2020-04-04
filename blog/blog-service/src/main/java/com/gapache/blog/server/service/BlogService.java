package com.gapache.blog.server.service;

import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.model.vo.ArchiveVO;
import com.gapache.commons.model.JsonResult;

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
     *  为Blog文档建立索引
     *
     * @param blog Blog文档
     */
    void index(Blog blog);

    /**
     * 查询博客
     *
     * @param id ID
     * @return 博客
     */
    JsonResult<Blog> get(String id);

    /**
     * 删除博客
     *
     * @param id ID
     * @return 操作结果
     */
    JsonResult<String> delete(String id);
}
