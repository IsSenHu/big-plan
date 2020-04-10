package com.gapache.blog.sdk.dubbo.tag;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/10 00:44
 */
public interface TagApiService {

    /**
     * 获取所有的标签信息
     *
     * @return 所有的标签信息
     */
    List<TagVO> findAll();
}
