package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.data.Tag;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/3 5:11 下午
 */
public interface TagRedisRepository {

    /**
     * 获取所有的标签
     *
     * @return 所有的标签
     */
    List<Tag> get();
}
