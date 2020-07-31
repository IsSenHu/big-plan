package com.gapache.cloud.auth.server.controller;

import com.gapache.cloud.auth.server.service.UserService;
import com.gapache.commons.model.JsonResult;
import com.gapache.security.model.UserLoginDTO;
import com.gapache.security.model.UserInfoDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @since 2020/7/31 2:01 下午
 */
@RestController
@RequestMapping("/auth/login")
public class LoginController {

    private final UserService userService;

    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public JsonResult<UserInfoDTO> login(@RequestBody UserLoginDTO dto) {
        UserInfoDTO result = userService.login(dto);
        return JsonResult.of(result);
    }
}
