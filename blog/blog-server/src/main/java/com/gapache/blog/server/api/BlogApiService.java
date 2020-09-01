package com.gapache.blog.server.api;

import com.gapache.blog.common.model.dto.BlogQueryDTO;
import com.gapache.blog.common.model.dto.BlogDTO;
import com.gapache.blog.common.model.dto.SimpleBlogDTO;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.PageResult;

/**
 * @author HuSen
 * create on 2020/4/5 03:30
 */
public interface BlogApiService {

    /**
     * 创建博客
     *
     * @param blog 博客
     * @return 是否成功
     */
    Boolean create(BlogDTO blog);

    /**
     * 根据ID查询博客
     *
     * @param id ID
     * @return 博客
     */
    SimpleBlogDTO get(String id);

    /**
     * 删除博客
     *
     * @param id ID
     * @return 是否成功
     */
    boolean delete(String id);

    /**
     * 更新博客
     *
     * @param blog 博客
     * @return 是否成功
     */
    Boolean update(BlogDTO blog);

    /**
     * 分页查询博客
     *
     * @param iPageRequest 分页参数
     * @return 分页结果
     */
    PageResult<SimpleBlogDTO> findAll(IPageRequest<BlogQueryDTO> iPageRequest);

    /**
     * 同步指定ID的博客到ES
     * 如果没有指定ID，则同步所有的博客
     *
     * @param id ID
     */
    void sync(String id);
}
