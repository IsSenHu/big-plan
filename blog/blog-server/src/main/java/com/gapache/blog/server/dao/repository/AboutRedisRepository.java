package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.data.About;

/**
 * @author HuSen
 * create on 2020/4/5 20:23
 */
public interface AboutRedisRepository {

    /**
     * 保存关于我
     *
     * @param about 关于我
     */
    void save(About about);

    /**
     * 获取关于我的信息
     *
     * @return 关于我
     */
    About get();
}
