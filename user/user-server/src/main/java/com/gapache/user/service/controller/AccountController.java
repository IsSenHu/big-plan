package com.gapache.user.service.controller;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.gapache.commons.model.JsonResult;
import com.gapache.user.service.model.AccountRegisterRequest;
import com.gapache.user.service.model.CheckAccountRequest;
import com.gapache.user.service.model.GenerateSmsCodeRequest;
import com.gapache.user.service.model.vo.AccountVO;
import com.gapache.user.service.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author HuSen
 * create on 2020/1/10 17:32
 */
@Slf4j
@RestController
@RequestMapping("/api/account")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/generateSmsCode")
    public JsonResult<Object> generateSmsCode(@RequestBody GenerateSmsCodeRequest request) {
        // 先生成验证码
        // 执行redis脚本 检查手机号 检查成功保存验证码
        // 发送验证码
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI4FooCvxWUJj6s7MCNLui", "o6Fg9AqRR7DzXNXUj186BvmhkNTWkT");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest commonRequest = new CommonRequest();
        commonRequest.setSysMethod(MethodType.POST);
        commonRequest.setSysDomain("dysmsapi.aliyuncs.com");
        commonRequest.setSysVersion("2017-05-25");
        commonRequest.setSysAction("SendSms");
        commonRequest.putQueryParameter("RegionId", "cn-hangzhou");
        commonRequest.putQueryParameter("PhoneNumbers", request.getThisPhone());
        commonRequest.putQueryParameter("SignName", "爱动网");
        commonRequest.putQueryParameter("TemplateCode", "SMS_129762977");
        commonRequest.putQueryParameter("TemplateParam", "{\"code\":\"234567\"}");
        try {
            CommonResponse response = client.getCommonResponse(commonRequest);
            log.info("短信发送结果:{}", response.getData());
        } catch (ClientException e) {
            log.error("短信发送失败:{}.", request.getThisPhone(), e);
        }
        return JsonResult.success();
    }

    @PutMapping
    public JsonResult<Object> register(@RequestBody AccountRegisterRequest request) {
        return accountService.register(request);
    }

    @PostMapping("/check")
    public JsonResult<AccountVO> check(@RequestBody CheckAccountRequest request) {
        return accountService.check(request);
    }
}
