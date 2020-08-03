package com.gapache.cloud.auth.server.service.impl;

import com.gapache.cloud.auth.server.dao.entity.ClientEntity;
import com.gapache.cloud.auth.server.dao.repository.ClientRepository;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.service.ClientService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/7/31 5:24 下午
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    private ClientRepository clientRepository;

    @Override
    public ClientDetailsImpl findByClientId(String clientId) {
        ClientEntity clientEntity = clientRepository.findByClientId(clientId);
        if (clientEntity == null) {
            return null;
        }
        ClientDetailsImpl clientDetails = new ClientDetailsImpl();
        clientDetails.setId(clientEntity.getId());
        clientDetails.setClientId(clientEntity.getClientId());
        clientDetails.setSecret(clientEntity.getSecret());
        clientDetails.setGrantTypes(clientEntity.getGrantTypes());
        clientDetails.setScopes(clientEntity.getScopes());
        clientDetails.setRedirectUrl(clientEntity.getRedirectUrl());
        return clientDetails;
    }
}
