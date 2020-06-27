package com.gapache.oms.store.location.server.trans;

import com.gapache.oms.store.location.sdk.model.vo.StoreVO;
import com.gapache.oms.store.location.server.dao.po.StorePO;
import org.springframework.beans.BeanUtils;

import java.util.function.Function;

/**
 * @author HuSen
 * @since 2020/6/27 7:23 下午
 */
public class  StoreVo2Po implements Function<StoreVO, StorePO> {

    @Override
    public StorePO apply(StoreVO vo) {
        StorePO po = new StorePO();
        BeanUtils.copyProperties(vo, po);
        return po;
    }
}
