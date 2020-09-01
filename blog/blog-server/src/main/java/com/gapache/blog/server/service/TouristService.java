package com.gapache.blog.server.service;

import com.gapache.blog.common.model.dto.TouristDTO;
import com.gapache.commons.model.JsonResult;

/**
 * @author HuSen
 * @since 2020/8/28 1:41 下午
 */
public interface TouristService {

    /**
     * 登陆
     *
     * @param dto 游客数据
     * @return 临时凭证
     */
    JsonResult<String> login(TouristDTO dto);

    /**
     * 注册
     *
     * @param dto 游客数据
     * @return 临时凭证
     */
    JsonResult<String> register(TouristDTO dto);
}
