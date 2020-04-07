package com.gapache.blog.server.service.impl;

import com.gapache.blog.server.dao.data.About;
import com.gapache.blog.server.dao.repository.AboutRepository;
import com.gapache.blog.server.service.AboutService;
import com.gapache.commons.model.JsonResult;
import org.springframework.stereotype.Service;

/**
 * @author HuSen
 * create on 2020/4/6 11:49
 */
@Service
public class AboutServiceImpl implements AboutService {

    private final AboutRepository aboutRepository;

    public AboutServiceImpl(AboutRepository aboutRepository) {
        this.aboutRepository = aboutRepository;
    }

    @Override
    public JsonResult<String> get() {
        About about = aboutRepository.get();
        return null;
    }
}
