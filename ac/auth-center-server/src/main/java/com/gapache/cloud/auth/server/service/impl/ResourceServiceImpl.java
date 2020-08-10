package com.gapache.cloud.auth.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.cloud.auth.server.dao.repository.ResourceRepository;
import com.gapache.security.interfaces.ResourceReceiver;
import com.gapache.security.model.AuthResourceDTO;
import com.gapache.security.model.ResourceReportDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * @since 2020/8/6 6:04 下午
 */
@Slf4j
@Service
public class ResourceServiceImpl implements ResourceReceiver {

    private final ResourceRepository resourceRepository;

    public ResourceServiceImpl(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean receiveReportData(ResourceReportDTO reportData) {
        log.info("receiveReportData:{}", JSON.toJSONString(reportData));
        String resourceServerName = reportData.getResourceServerName();
        List<AuthResourceDTO> authResources = reportData.getAuthResources();
        Set<String> newScope = authResources.stream().map(AuthResourceDTO::getScope).collect(Collectors.toSet());
        List<ResourceEntity> old = resourceRepository.findAllByResourceServerName(resourceServerName);
        // 待删除
        List<ResourceEntity> delete = new ArrayList<>();
        // 待更新
        List<ResourceEntity> update = new ArrayList<>();
        // 待添加
        List<ResourceEntity> add = new ArrayList<>();

        Map<String, ResourceEntity> oldScopeMap = old.stream().peek(r -> {
            if (!newScope.contains(r.getScope())) {
                delete.add(r);
            }
        }).collect(Collectors.toMap(ResourceEntity::getScope, e -> e));

        authResources.forEach(dto -> {
            if (oldScopeMap.containsKey(dto.getScope())) {
                ResourceEntity resourceEntity = oldScopeMap.get(dto.getScope());
                if (!StringUtils.equals(resourceEntity.getName(), dto.getName())) {
                    resourceEntity.setName(dto.getName());
                    update.add(resourceEntity);
                }
            }  else {
                ResourceEntity entity = new ResourceEntity();
                entity.setResourceServerName(resourceServerName);
                entity.setScope(dto.getScope());
                entity.setName(dto.getName());
                add.add(entity);
            }
        });
        log.info("receiveReportData delete:{}", JSON.toJSONString(delete));
        resourceRepository.deleteInBatch(delete);
        log.info("receiveReportData update:{}", JSON.toJSONString(update));
        resourceRepository.saveAll(update);
        log.info("receiveReportData add:{}", JSON.toJSONString(add));
        resourceRepository.saveAll(add);
        return true;
    }
}
