package com.gapache.mybatis.demo.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.gapache.mybatis.demo.dao.po.OrderPO;
import org.springframework.stereotype.Repository;

/**
 * @author HuSen
 * create on 2020/5/13 9:47 上午
 */
@Repository
public interface OrderMapper extends BaseMapper<OrderPO> {

    IPage<OrderPO> page(IPage<OrderPO> page);
}
