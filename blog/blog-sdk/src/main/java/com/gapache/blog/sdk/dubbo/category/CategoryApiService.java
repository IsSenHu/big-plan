package com.gapache.blog.sdk.dubbo.category;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/10 00:44
 */
public interface CategoryApiService {

    /**
     * 获得所有的分类
     *
     * @return 所有分类
     */
    List<CategoryVO> findAll();
}
