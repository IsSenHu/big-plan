package com.gapache.blog.server.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gapache.blog.common.model.dto.ReviewDTO;
import com.gapache.blog.common.model.dto.TouristDTO;
import com.gapache.blog.common.model.error.BlogError;
import com.gapache.blog.server.dao.entity.ReviewEntity;
import com.gapache.blog.server.dao.repository.ReviewRepository;
import com.gapache.blog.server.service.ReviewService;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author HuSen
 * @since 2020/8/28 9:28 上午
 */
@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final StringRedisTemplate redisTemplate;

    public ReviewServiceImpl(ReviewRepository reviewRepository, StringRedisTemplate redisTemplate) {
        this.reviewRepository = reviewRepository;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public JsonResult<Boolean> create(String touristToken, ReviewDTO dto) {
        TouristDTO tourist = getTourist(touristToken);
        ThrowUtils.throwIfTrue(tourist == null, BlogError.NOT_LOGIN);

        ReviewEntity reviewEntity = new ReviewEntity();
        reviewEntity.setOtherId(dto.getOtherId());
        reviewEntity.setContent(dto.getContent());
        reviewEntity.setParentId(dto.getParentId());
        reviewEntity.setReplyRelationId(dto.getReplyRelationId());
        reviewEntity.setTime(LocalDateTime.now());
        reviewEntity.setWhoId(tourist.getId());
        reviewEntity.setWho(tourist.getNick());
        reviewRepository.save(reviewEntity);
        return JsonResult.of(true);
    }

    

    private TouristDTO getTourist(String touristToken) {
        String touristInfo = redisTemplate.opsForValue().get(touristToken);
        if (StringUtils.isBlank(touristInfo)) {
            return null;
        }
        return JSONObject.parseObject(touristInfo, TouristDTO.class);
    }
}
