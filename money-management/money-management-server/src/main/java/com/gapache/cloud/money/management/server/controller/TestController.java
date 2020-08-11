package com.gapache.cloud.money.management.server.controller;

import com.gapache.commons.model.JsonResult;
import com.gapache.security.annotation.AuthResource;
import com.gapache.security.annotation.NeedAuth;
import com.gapache.security.holder.AccessCardHolder;
import com.gapache.security.model.AccessCard;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * @since 2020/8/10 7:31 下午
 */
@NeedAuth("Test")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @PostMapping("/checkAccessCard")
    @AuthResource(scope = "checkAccessCard", name = "检查通行证")
    public JsonResult<AccessCard> checkAccessCard() {
        AccessCard context = AccessCardHolder.getContext();
        return JsonResult.of(context);
    }
}
