package com.gapache.health.server.transfer;

import com.gapache.health.server.dao.po.BodyDataPO;
import com.gapache.health.server.model.body.BodyDataVO;

import java.util.function.Function;

/**
 * @author HuSen
 * create on 2020/5/6 2:16 下午
 */
public class BodyDataPo2Vo implements Function<BodyDataPO, BodyDataVO> {

    @Override
    public BodyDataVO apply(BodyDataPO po) {
        BodyDataVO vo = new BodyDataVO();
        vo.setId(po.getId());
        vo.setBodyScore(po.getBodyScore());
        vo.setBasalMetabolism(po.getBasalMetabolism());
        vo.setBmi(po.getBmi());
        vo.setBodyFat(po.getBodyFat());
        vo.setBodyType(po.getBodyType());
        vo.setBoneMass(po.getBoneMass());
        vo.setCheckTime(po.getCheckTime());
        vo.setMoisture(po.getMoisture());
        vo.setMuscle(po.getMuscle());
        vo.setProtein(po.getProtein());
        vo.setVisceralFat(po.getVisceralFat());
        vo.setWeight(po.getWeight());
        vo.setBodyAge(po.getBodyAge());
        return vo;
    }
}
