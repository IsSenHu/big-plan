package com.gapache.blog.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gapache.blog.common.model.dto.TouristDTO;
import com.gapache.blog.common.model.error.BlogError;
import com.gapache.blog.server.dao.entity.TouristEntity;
import com.gapache.blog.server.dao.repository.TouristRepository;
import com.gapache.blog.server.service.TouristService;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.commons.security.HashUtils;
import com.gapache.commons.utils.IStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

/**
 * @author HuSen
 * @since 2020/8/28 1:41 下午
 */
@Service
public class TouristServiceImpl implements TouristService {

    private static final String BLOG_TOURIST_TOKEN_PREFIX = "blogTouristToken:";

    private final TouristRepository touristRepository;
    private final StringRedisTemplate redisTemplate;

    public TouristServiceImpl(TouristRepository touristRepository, StringRedisTemplate redisTemplate) {
        this.touristRepository = touristRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public JsonResult<String> login(TouristDTO dto) {
        TouristEntity entity = touristRepository.findByEmail(dto.getEmail());
        ThrowUtils.throwIfTrue(entity == null, BlogError.VERIFY_TOURIST_FAIL);

        String password = dto.getPassword();
        String encrypt = HashUtils.encrypt(IStringUtils.getBytes(password), HashUtils.MD5);
        ThrowUtils.throwIfTrue(!StringUtils.equals(encrypt, entity.getPassword()), BlogError.VERIFY_TOURIST_FAIL);

        String token = generateToken(entity);
        return JsonResult.of(token);
    }

    @Override
    public JsonResult<String> register(TouristDTO dto) {
        boolean existsByEmail = touristRepository.existsByEmail(dto.getEmail());
        ThrowUtils.throwIfTrue(existsByEmail, BlogError.EXISTS_BY_EMAIL);

        boolean existsByNick = touristRepository.existsByNick(dto.getNick());
        ThrowUtils.throwIfTrue(existsByNick, BlogError.EXISTS_BY_NICK);

        TouristEntity entity = new TouristEntity();
        entity.setEmail(dto.getEmail());
        entity.setNick(dto.getNick());
        String encrypt = HashUtils.encrypt(IStringUtils.getBytes(dto.getPassword()), HashUtils.MD5);
        entity.setPassword(encrypt);
        touristRepository.save(entity);

        String token = generateToken(entity);
        return JsonResult.of(token);
    }

    private String generateToken(TouristEntity entity) {
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(
                BLOG_TOURIST_TOKEN_PREFIX.concat(token), JSONObject.toJSONString(entity), Duration.ofDays(1)
        );
        return token;
    }
}
