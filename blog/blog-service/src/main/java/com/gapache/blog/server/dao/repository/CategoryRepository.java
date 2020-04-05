package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.data.Category;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/4 22:35
 */
public interface CategoryRepository {

    /**
     * 查询所有的分裂
     *
     * @return 所有的分类
     */
    List<Category> get();

    /**
     * 新增分类，如果存在分数加1
     *
     * @param category 分类
     */
    void add(String category);

    /**
     * 删除分类，其实是将分类的分数减1，如果为0则删除
     *
     * @param category 分类
     */
    void delete(String category);


    /**
     * 先删除再新增
     *
     * @param delete 删除的
     * @param add    新增的
     */
    void deleteThenAdd(String delete, String add);
}
