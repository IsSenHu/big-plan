package com.gapache.cloud.stream;

import com.gapache.commons.model.JsonResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/11 5:55 下午
 */
@RestController
@RequestMapping("/api/message")
public class SendMessageController {

    @Resource
    private IMessageProvider iMessageProvider;

    @GetMapping("/send")
    public JsonResult<String> send() {
        return JsonResult.of(iMessageProvider.send());
    }
}
