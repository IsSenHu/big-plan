package com.gapache.blog.server.api;

import com.alibaba.dubbo.config.annotation.Service;
import com.gapache.blog.server.dao.repository.AboutRedisRepository;
import com.gapache.blog.server.dao.data.About;
import com.gapache.blog.sdk.dubbo.about.AboutApiService;
import com.gapache.blog.sdk.dubbo.about.AboutVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * @author HuSen
 * create on 2020/4/5 20:28
 */
@Service
@Component
public class AboutApiServiceImpl implements AboutApiService {

    private final AboutRedisRepository aboutRedisRepository;

    public AboutApiServiceImpl(AboutRedisRepository aboutRedisRepository) {
        this.aboutRedisRepository = aboutRedisRepository;
    }

    @Override
    public void save(AboutVO vo) {
        About about = new About();
        BeanUtils.copyProperties(vo, about);
        aboutRedisRepository.save(about);
    }
}
