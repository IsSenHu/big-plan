package com.gapache.blog.server.service.impl;

import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogRepository;
import com.gapache.blog.server.dao.repository.CategoryRepository;
import com.gapache.blog.server.dao.repository.TagRepository;
import com.gapache.blog.server.model.BlogError;
import com.gapache.blog.server.model.vo.ArchiveItemVO;
import com.gapache.blog.server.model.vo.ArchiveVO;
import com.gapache.blog.server.service.BlogService;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.commons.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author HuSen
 * create on 2020/4/3 1:15 下午
 */
@Slf4j
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogRepository blogRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;

    public BlogServiceImpl(BlogRepository blogRepository, TagRepository tagRepository, CategoryRepository categoryRepository) {
        this.blogRepository = blogRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public JsonResult<ArchiveVO> archive() {
        SearchResponse archly = blogRepository.archly();
        SearchHits searchHits = archly.getHits();
        log.info("共归档到{}个文件", searchHits.getTotalHits().value);

        List<ArchiveItemVO> items = new ArrayList<>();
        for (SearchHit hit : searchHits.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            ArchiveItemVO item = new ArchiveItemVO();
            item.setId(hit.getId());
            item.setTitle((String) sourceAsMap.get("title"));
            item.setPublishTime(TimeUtils.parse(TimeUtils.Format._3, sourceAsMap.get("publishTime").toString()));
            items.add(item);
        }

        Map<LocalDate, List<ArchiveItemVO>> map = items.stream().collect(Collectors.groupingBy(item -> item.getPublishTime().toLocalDate()));

        ArchiveVO vo = new ArchiveVO();
        vo.setCount(searchHits.getTotalHits().value);
        List<Map<String, Object>> timeline = new ArrayList<>();
        for (Map.Entry<LocalDate, List<ArchiveItemVO>> entry : map.entrySet()) {
            LocalDate localDate = entry.getKey();
            List<ArchiveItemVO> vos = entry.getValue();
            Map<String, Object> lineItem = new HashMap<>(2);
            lineItem.put("date", TimeUtils.format(TimeUtils.Format._1, localDate));
            lineItem.put("items", vos);
            timeline.add(lineItem);
        }

        timeline.sort((o1, o2) -> StringUtils.compare(o2.get("date").toString(), o1.get("date").toString()));
        vo.setTimeline(timeline);

        return JsonResult.of(vo);
    }

    @Override
    public void index(Blog blog) {
        IndexResponse response = blogRepository.index(blog);
        if (response == null || !response.status().equals(RestStatus.OK)) {
            return;
        }
        tagRepository.increment(blog.getTags());
        categoryRepository.add(blog.getCategory());
    }

    @Override
    public JsonResult<Blog> get(String id) {
        Blog blog = blogRepository.get(id);
        ThrowUtils.throwIfTrue(blog == null, BlogError.NOT_FOUND);
        return JsonResult.of(blog);
    }

    @Override
    public JsonResult<String> delete(String id) {
        ThrowUtils.throwIfTrue(!blogRepository.delete(id), BlogError.OP_ERROR);
        return JsonResult.of(id);
    }
}
