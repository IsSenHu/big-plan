package com.gapache.blog.sdk.fallback;

import com.gapache.blog.common.model.dto.*;
import com.gapache.blog.sdk.feign.BlogServerFeign;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/26 9:38 上午
 */
@Slf4j
@Component
public class BlogServerFeignFallback implements BlogServerFeign {

    @Override
    public JsonResult<Boolean> saveAbout(AboutDTO dto) {
        return null;
    }

    @Override
    public JsonResult<List<CategoryDTO>> findAllCategory() {
        return null;
    }

    @Override
    public JsonResult<List<TagDTO>> findAllTag() {
        return null;
    }

    @Override
    public JsonResult<Boolean> create(BlogDTO blog) {
        return null;
    }

    @Override
    public JsonResult<SimpleBlogDTO> getSimple(String id) {
        return null;
    }

    @Override
    public JsonResult<Boolean> delete(String id) {
        return null;
    }

    @Override
    public JsonResult<Boolean> update(BlogDTO blog) {
        return null;
    }

    @Override
    public JsonResult<PageResult<SimpleBlogDTO>> findAll(IPageRequest<BlogQueryDTO> iPageRequest) {
        return null;
    }

    @Override
    public JsonResult<Boolean> sync(String id) {
        return null;
    }
}
