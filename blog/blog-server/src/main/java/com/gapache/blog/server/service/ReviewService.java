package com.gapache.blog.server.service;

import com.gapache.blog.common.model.dto.ReviewDTO;
import com.gapache.commons.model.JsonResult;

/**
 * @author HuSen
 * @since 2020/8/28 9:27 上午
 */
public interface ReviewService {

    /**
     * 创建评论
     *
     * @param touristToken 游客凭证
     * @param dto          评论信息
     * @return 创建结果
     */
    JsonResult<Boolean> create(String touristToken, ReviewDTO dto);
}
