package com.gapache.auth.server.controller;

import com.gapache.auth.server.model.LoginVO;
import com.gapache.auth.server.model.TokenVO;
import com.gapache.auth.server.model.UserInfoVO;
import com.gapache.commons.model.JsonResult;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @author HuSen
 * create on 2020/4/7 3:18 下午
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public JsonResult<TokenVO> login(@RequestBody LoginVO login) {
        log.info("login:{}", login);
        UserInfoVO info = new UserInfoVO("HuSen", "admin", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif", Lists.newArrayList("admin"));
        return JsonResult.of(new TokenVO(UUID.randomUUID().toString(), info));
    }

    @PostMapping("/logout")
    public JsonResult<Object> logout(HttpServletRequest request) {
        return JsonResult.success();
    }

    @PostMapping("/check")
    public JsonResult<Boolean> check(HttpServletRequest request) {
        return JsonResult.of(Boolean.TRUE);
    }

    @PostMapping("/lock")
    public JsonResult<Object> lock(HttpServletRequest request) {
        return JsonResult.success();
    }

    @PostMapping("/unlock/{password}")
    public JsonResult<Object> unlock(@PathVariable String password, HttpServletRequest request) {
        return JsonResult.success();
    }

    @PostMapping("/isLocked")
    public JsonResult<Boolean> isLocked() {
        return JsonResult.of(Boolean.FALSE);
    }
}
