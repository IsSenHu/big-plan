package com.gapache.health.server.service.impl;

import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import com.gapache.health.server.dao.po.BodyDataPO;
import com.gapache.health.server.dao.repository.BodyDataRepository;
import com.gapache.health.server.model.body.BodyDataCreateVO;
import com.gapache.health.server.model.body.BodyDataQueryVO;
import com.gapache.health.server.model.body.BodyDataVO;
import com.gapache.health.server.service.BodyDataService;
import com.gapache.health.server.transfer.BodyDataPo2Vo;
import com.gapache.jpa.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/5/6 10:06 上午
 */
@Service
public class BodyDataServiceImpl implements BodyDataService {
    private final BodyDataRepository bodyDataRepository;

    public BodyDataServiceImpl(BodyDataRepository bodyDataRepository) {
        this.bodyDataRepository = bodyDataRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<Long> create(BodyDataCreateVO vo) {
        LocalDateTime checkTime = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(checkTime.getYear(), checkTime.getMonthValue(), checkTime.getDayOfMonth(), 0, 0, 0);
        LocalDateTime end = start.plusDays(1);
        List<BodyDataPO> oldList = bodyDataRepository.findAllByCheckTimeLessThanAndCheckTimeGreaterThanEqual(end, start);
        // 只保留最新的
        bodyDataRepository.deleteAll(oldList);
        BodyDataPO po = new BodyDataPO();
        BeanUtils.copyProperties(vo, po);
        po.setCheckTime(checkTime);
        bodyDataRepository.save(po);
        return JsonResult.of(po.getId());
    }

    @Override
    public JsonResult<List<BodyDataVO>> statistics() {
        List<BodyDataPO> all = bodyDataRepository.findAll(Sort.by("checkTime").ascending());
        List<BodyDataVO> result = all.stream().map(po -> new BodyDataPo2Vo().apply(po)).collect(Collectors.toList());
        return JsonResult.of(result);
    }

    @Override
    public JsonResult<PageResult<BodyDataVO>> page(IPageRequest<BodyDataQueryVO> request) {
        Pageable pageable = PageHelper.of(request);
        Page<BodyDataPO> page = bodyDataRepository.findAll(pageable);
        return JsonResult.of(PageResult.of(page.getTotalElements(), po -> new BodyDataPo2Vo().apply(po), page.get()));
    }
}