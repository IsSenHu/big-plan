package com.gapache.blog.server.api.impl;

import com.gapache.blog.common.model.dto.AboutDTO;
import com.gapache.blog.server.api.AboutApiService;
import com.gapache.blog.server.dao.data.About;
import com.gapache.blog.server.dao.repository.AboutRedisRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * create on 2020/4/5 20:28
 */
@Service
public class AboutApiServiceImpl implements AboutApiService {

    private final AboutRedisRepository aboutRedisRepository;

    public AboutApiServiceImpl(AboutRedisRepository aboutRedisRepository) {
        this.aboutRedisRepository = aboutRedisRepository;
    }

    @Override
    public void save(AboutDTO vo) {
        About about = new About();
        BeanUtils.copyProperties(vo, about);
        aboutRedisRepository.save(about);
    }
}
