package com.gapache.blog.server.service;

import com.gapache.commons.model.JsonResult;

/**
 * @author HuSen
 * create on 2020/4/6 11:49
 */
public interface AboutService {

    /**
     * 获取关于我的信息
     *
     * @return 关于我
     */
    JsonResult<String> get();
}
