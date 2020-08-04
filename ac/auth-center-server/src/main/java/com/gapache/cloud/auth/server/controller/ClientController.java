package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.model.UserClientRelationDTO;
import com.gapache.cloud.auth.server.service.ClientService;
import com.gapache.cloud.auth.server.service.UserClientRelationService;
import com.gapache.commons.model.JsonResult;
import com.gapache.security.model.ClientDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @since 2020/8/3 11:34 上午
 */
@RestController
@RequestMapping("/api/client")
public class ClientController {

    private final ClientService clientService;

    private final UserClientRelationService userClientRelationService;

    public ClientController(ClientService clientService, UserClientRelationService userClientRelationService) {
        this.clientService = clientService;
        this.userClientRelationService = userClientRelationService;
    }

    @PostMapping
    public JsonResult<Boolean> create(@RequestBody ClientDTO clientDTO) {
        return JsonResult.of(clientService.create(clientDTO));
    }

    @PostMapping("/bindUser")
    public JsonResult<Boolean> bindUser(@RequestBody UserClientRelationDTO userClientRelationDTO) {
        return JsonResult.of(userClientRelationService.create(userClientRelationDTO));
    }
}
