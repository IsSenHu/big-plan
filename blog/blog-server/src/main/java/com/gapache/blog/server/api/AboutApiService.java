package com.gapache.blog.server.api;

import com.gapache.blog.common.model.dto.AboutDTO;

/**
 * @author HuSen
 * create on 2020/4/5 20:27
 */
public interface AboutApiService {

    /**
     * 保存关于我
     *
     * @param dto 关于我
     */
    void save(AboutDTO dto);
}
