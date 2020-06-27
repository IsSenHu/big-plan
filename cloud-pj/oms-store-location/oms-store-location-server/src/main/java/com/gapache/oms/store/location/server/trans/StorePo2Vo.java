package com.gapache.oms.store.location.server.trans;

import com.gapache.oms.store.location.sdk.model.vo.StoreVO;
import com.gapache.oms.store.location.server.dao.po.StorePO;
import org.springframework.beans.BeanUtils;

import java.util.function.Function;

/**
 * @author HuSen
 * @since 2020/6/23 11:30 上午
 */
public class StorePo2Vo implements Function<StorePO, StoreVO> {

    @Override
    public StoreVO apply(StorePO po) {
        StoreVO vo = new StoreVO();
        BeanUtils.copyProperties(po, vo);
        return vo;
    }
}
