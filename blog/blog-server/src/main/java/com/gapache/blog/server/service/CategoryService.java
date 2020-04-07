package com.gapache.blog.server.service;

import com.gapache.blog.server.dao.data.Category;
import com.gapache.commons.model.JsonResult;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/4/4 22:34
 */
public interface CategoryService {

    /**
     * 查询所有的分裂
     *
     * @return 所有的分类
     */
    JsonResult<List<Category>> get();
}
