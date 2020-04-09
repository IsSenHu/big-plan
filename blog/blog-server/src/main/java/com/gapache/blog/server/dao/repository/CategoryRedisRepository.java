package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.data.Category;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/4 22:35
 */
public interface CategoryRedisRepository {

    /**
     * 查询所有的分裂
     *
     * @return 所有的分类
     */
    List<Category> get();
}
