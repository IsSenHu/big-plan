package com.gapache.health.server.service;

import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import com.gapache.health.server.model.body.BodyDataCreateVO;
import com.gapache.health.server.model.body.BodyDataQueryVO;
import com.gapache.health.server.model.body.BodyDataVO;

import java.util.List;

/**
 * @author HuSen
 * create on 2020/5/6 10:06 上午
 */
public interface BodyDataService {
    /**
     * 创建身体数据
     *
     * @param vo 创建身体数据数据模型
     * @return 创建结果
     */
    JsonResult<Long> create(BodyDataCreateVO vo);

    /**
     * 统计身体数据
     *
     * @return 统计结果
     */
    JsonResult<List<BodyDataVO>> statistics();

    /**
     * 分页查询身体数据
     *
     * @param request 分页请求
     * @return 分页数据
     */
    JsonResult<PageResult<BodyDataVO>> page(IPageRequest<BodyDataQueryVO> request);
}
