package com.gapache.blog.server.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gapache.blog.common.model.dto.FriendlyLinkDTO;
import com.gapache.blog.server.service.OtherService;
import com.gapache.commons.model.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/28 9:30 上午
 */
@Slf4j
@Service
public class OtherServiceImpl implements OtherService {

    private static final String WX_QR_CODE_KEY = "wxQrCode19950528";
    private static final String FRIENDLY_LINK_KEY = "friendlyLink19950528";

    private final StringRedisTemplate redisTemplate;

    public OtherServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public JsonResult<String> wxQrCode() {
        String value = redisTemplate.opsForValue().get(WX_QR_CODE_KEY);
        return JsonResult.of(value);
    }

    @Override
    public JsonResult<List<FriendlyLinkDTO>> friendlyLinks() {
        String value = redisTemplate.opsForValue().get(FRIENDLY_LINK_KEY);
        if (StringUtils.isBlank(value)) {
            return JsonResult.of(new ArrayList<>());
        }
        List<FriendlyLinkDTO> result = JSONArray.parseArray(value, FriendlyLinkDTO.class);
        return JsonResult.of(result);
    }

    @Override
    public JsonResult<Boolean> saveWxQrCode(String link) {
        try {
            redisTemplate.opsForValue().set(WX_QR_CODE_KEY, link);
            return JsonResult.of(true);
        } catch (Exception e) {
            log.error("saveWxQrCode error.", e);
        }
        return JsonResult.of(false);
    }

    @Override
    public JsonResult<Boolean> saveFriendlyLinks(List<FriendlyLinkDTO> list) {
        try {
            redisTemplate.opsForValue().set(FRIENDLY_LINK_KEY, JSONObject.toJSONString(list));
            return JsonResult.of(true);
        } catch (Exception e) {
            log.error("saveFriendlyLinks error.", e);
        }
        return JsonResult.of(false);
    }
}
