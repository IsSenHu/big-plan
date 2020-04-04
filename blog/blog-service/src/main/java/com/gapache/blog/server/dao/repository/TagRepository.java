package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.ro.Tag;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/3 5:11 下午
 */
public interface TagRepository {

    /**
     * 获取所有的标签
     *
     * @return 所有的标签
     */
    List<Tag> get();

    /**
     * 给指定标签计数加1
     *
     * @param tags 指定标签
     */
    void increment(String[] tags);

    /**
     * 给指定的标签计数减1
     *
     * @param tags 指定标签
     */
    void decrement(String[] tags);
}
