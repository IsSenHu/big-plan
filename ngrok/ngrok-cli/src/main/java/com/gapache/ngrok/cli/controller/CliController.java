package com.gapache.ngrok.cli.controller;

import com.gapache.commons.model.Error;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.ngrok.cli.http.HttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author HuSen
 * create on 2020/4/14 3:50 下午
 */
@RestController
@RequestMapping("/api/cli")
public class CliController {

    @GetMapping
    public JsonResult<Object> newCli(String serverIp, int serverPort, String innerIp, int innerPort, String name) {
        ThrowUtils.throwIfTrue(StringUtils.isBlank(serverIp) || serverPort == 0 || StringUtils.isBlank(innerIp) || innerPort == 0 || StringUtils.isBlank(name), new Error() {
            @Override
            public Integer getCode() {
                return 99999999;
            }

            @Override
            public String getError() {
                return " 错误的参数!";
            }
        });
        client(serverIp, serverPort, innerIp, innerPort, name);
        return JsonResult.success();
    }

    private static void client(String serverIp, int serverPort, String innerIp, int innerPort, String name) {
        HttpClient httpClient = new HttpClient(serverIp, serverPort, innerPort, innerIp, name, 100);
        httpClient.init();
        httpClient.start();
    }
}
