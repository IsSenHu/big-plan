package com.gapache.cloud.auth.server.service.impl;

import com.gapache.cloud.auth.server.dao.entity.UserClientRelationEntity;
import com.gapache.cloud.auth.server.dao.repository.UserClientRelationRepository;
import com.gapache.cloud.auth.server.model.UserClientRelationDTO;
import com.gapache.cloud.auth.server.service.UserClientRelationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * @since 2020/8/3 9:11 上午
 */
@Service
public class UserClientRelationServiceImpl implements UserClientRelationService {

    private final UserClientRelationRepository userClientRelationRepository;

    public UserClientRelationServiceImpl(UserClientRelationRepository userClientRelationRepository) {
        this.userClientRelationRepository = userClientRelationRepository;
    }

    @Override
    public UserClientRelationDTO findByUserIdAndClientId(Long userId, Long clientId) {
        UserClientRelationEntity entity = userClientRelationRepository.findByUserIdAndClientId(userId, clientId);
        if (entity == null) {
            return null;
        }
        UserClientRelationDTO dto = new UserClientRelationDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
}
