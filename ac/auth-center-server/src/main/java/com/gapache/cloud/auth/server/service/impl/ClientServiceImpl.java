package com.gapache.cloud.auth.server.service.impl;

import com.gapache.cloud.auth.server.dao.entity.ClientEntity;
import com.gapache.cloud.auth.server.dao.repository.ClientRepository;
import com.gapache.cloud.auth.server.model.ClientDetailsImpl;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.security.model.ClientDTO;
import com.gapache.security.model.SecurityError;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * @author HuSen
 * @since 2020/7/31 5:24 下午
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Resource
    private ClientRepository clientRepository;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Override
    public ClientDetailsImpl findByClientId(String clientId) {
        ClientEntity clientEntity = clientRepository.findByClientId(clientId);
        if (clientEntity == null) {
            return null;
        }
        ClientDetailsImpl clientDetails = new ClientDetailsImpl();
        BeanUtils.copyProperties(clientEntity, clientDetails);
        clientDetails.setId(clientEntity.getId());
        return clientDetails;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean create(ClientDTO clientDTO) {
        Boolean exists = clientRepository.existsByClientId(clientDTO.getClientId());
        ThrowUtils.throwIfTrue(exists, SecurityError.CLIENT_EXISTED);

        ClientEntity entity = new ClientEntity();
        BeanUtils.copyProperties(clientDTO, entity, "secret");
        entity.setSecret(passwordEncoder.encode(clientDTO.getSecret()));
        return clientRepository.save(entity).getId() != null;
    }

    @Override
    public ClientDetailsImpl findById(Long id) {
        Optional<ClientEntity> optional = clientRepository.findById(id);
        return optional.map(clientEntity -> {
            ClientDetailsImpl clientDetails = new ClientDetailsImpl();
            BeanUtils.copyProperties(clientEntity, clientDetails);
            return clientDetails;
        }).orElse(null);
    }
}
