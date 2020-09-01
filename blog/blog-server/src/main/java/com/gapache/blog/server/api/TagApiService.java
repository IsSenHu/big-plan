package com.gapache.blog.server.api;

import com.gapache.blog.common.model.dto.TagDTO;

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
    List<TagDTO> findAll();
}
