package com.gapache.blog.server.service;

import com.gapache.blog.server.dao.data.Tag;
import com.gapache.commons.model.JsonResult;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/3 5:12 下午
 */
public interface TagService {

    /**
     * 获取所有的标签
     *
     * @return 所有的标签
     */
    JsonResult<List<Tag>> get();
}
