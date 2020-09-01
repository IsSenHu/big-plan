package com.gapache.blog.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.gapache.blog.common.model.dto.*;
import com.gapache.blog.server.dao.data.BlogData;
import com.gapache.blog.server.dao.document.Blog;
import com.gapache.blog.server.dao.repository.BlogEsRepository;
import com.gapache.blog.server.lua.ViewsLuaScript;
import com.gapache.blog.common.model.error.BlogError;
import com.gapache.blog.server.service.BlogService;
import com.gapache.commons.model.IPageRequest;
import com.gapache.commons.model.JsonResult;
import com.gapache.commons.model.ThrowUtils;
import com.gapache.commons.utils.IStringUtils;
import com.gapache.commons.utils.TimeUtils;
import com.gapache.protobuf.utils.ProtocstuffUtils;
import com.gapache.redis.RedisLuaExecutor;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

import static com.gapache.blog.server.dao.data.Structures.*;

/**
 * @author HuSen
 * create on 2020/4/3 1:15 下午
 */
@Slf4j
@Service
public class BlogServiceImpl implements BlogService {

    private final BlogEsRepository blogEsRepository;
    private final RedisLuaExecutor luaExecutor;
    private final StringRedisTemplate redisTemplate;

    private final ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    @PostConstruct
    public void init() {
        taskExecutor.setCorePoolSize(1);
        taskExecutor.setMaxPoolSize(1);
        taskExecutor.setQueueCapacity(1000);
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        taskExecutor.setThreadFactory(r -> new Thread(r, "views thread"));
        taskExecutor.initialize();
    }

    public BlogServiceImpl(BlogEsRepository blogEsRepository, RedisLuaExecutor luaExecutor, StringRedisTemplate redisTemplate) {
        this.blogEsRepository = blogEsRepository;
        this.luaExecutor = luaExecutor;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public JsonResult<ArchiveDTO> archive() {
        SearchResponse archly = blogEsRepository.archly();
        SearchHits searchHits = archly.getHits();
        log.info("共归档到{}个文件", searchHits.getTotalHits().value);

        List<ArchiveItemDTO> items = new ArrayList<>();
        for (SearchHit hit : searchHits.getHits()) {
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            ArchiveItemDTO item = new ArchiveItemDTO();
            item.setId(hit.getId());
            item.setTitle((String) sourceAsMap.get("title"));
            item.setPublishTime(TimeUtils.parse(TimeUtils.Format._3, sourceAsMap.get("publishTime").toString()));
            items.add(item);
        }

        Map<LocalDate, List<ArchiveItemDTO>> map = items.stream().collect(Collectors.groupingBy(item -> item.getPublishTime().toLocalDate()));

        ArchiveDTO vo = new ArchiveDTO();
        vo.setCount(searchHits.getTotalHits().value);
        List<Map<String, Object>> timeline = new ArrayList<>();
        for (Map.Entry<LocalDate, List<ArchiveItemDTO>> entry : map.entrySet()) {
            LocalDate localDate = entry.getKey();
            List<ArchiveItemDTO> vos = entry.getValue();
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
    public JsonResult<FullBlogDTO> get(String id) {
        FullBlogDTO dto = new FullBlogDTO();
        String simple = redisTemplate.opsForValue().get(BLOG.key(id));
        ThrowUtils.throwIfTrue(StringUtils.isBlank(simple), BlogError.NOT_FOUND);
        BlogData blogData = JSON.parseObject(simple, BlogData.class);
        BeanUtils.copyProperties(blogData, dto, "content", "views");

        Double score = redisTemplate.opsForZSet().score(VIEWS.key(), id);
        dto.setViews(score != null ? score.intValue() : 0);

        MarkdownDTO md = redisTemplate.execute((RedisCallback<MarkdownDTO>) connection -> {
            byte[] bytes = connection.get(CONTENT.keyBytes(id));
            if (bytes == null) {
                return new MarkdownDTO();
            }
            return ProtocstuffUtils.byte2Bean(bytes, MarkdownDTO.class);
        });

        dto.setContent(md != null ? md.getItems() : null);
        return JsonResult.of(dto);
    }

    @Override
    public JsonResult<Object> views(String id) {
        taskExecutor.execute(() ->
                luaExecutor.execute(ViewsLuaScript.INCREMENT, Collections.singletonList(VIEWS.key()), id));
        return JsonResult.success();
    }

    @Override
    public JsonResult<List<RankDTO<SimpleBlogDTO>>> top(Integer from, Integer number) {
        ZSetOperations<String, String> zSet = redisTemplate.opsForZSet();
        Set<ZSetOperations.TypedTuple<String>> withScores = zSet.reverseRangeWithScores(VIEWS.key(), from, from + number - 1);
        if (CollectionUtils.isEmpty(withScores)) {
            return JsonResult.of(Lists.newArrayList());
        }
        Set<String> ids = new HashSet<>(withScores.size());
        Map<String, Integer> scoreMap = new HashMap<>(withScores.size());
        for (ZSetOperations.TypedTuple<String> idWithScore : withScores) {
            ids.add(idWithScore.getValue());
            Double score = idWithScore.getScore();
            scoreMap.put(idWithScore.getValue(), score == null ? 0 : score.intValue());
        }

        List<SimpleBlogDTO> allByIds = findAllByIds(ids, scoreMap);

        List<RankDTO<SimpleBlogDTO>> result = new ArrayList<>(withScores.size());
        allByIds.sort(((o1, o2) -> o2.getViews() - o1.getViews()));
        int size = allByIds.size();
        for (int i = 0; i < size; i++) {
            RankDTO<SimpleBlogDTO> rank = new RankDTO<>();
            rank.setData(allByIds.get(i));
            rank.setRank(i + 1);
            result.add(rank);
        }

        return JsonResult.of(result);
    }

    @Override
    public JsonResult<List<RankDTO<SimpleBlogDTO>>> find(IPageRequest<BlogQueryDTO> iPageRequest) {
        List<SimpleBlogDTO> list = trans2VoList(blogEsRepository.find(iPageRequest));
        List<RankDTO<SimpleBlogDTO>> result = new ArrayList<>();
        for (SimpleBlogDTO dto : list) {
            RankDTO<SimpleBlogDTO> rank = new RankDTO<>();
            rank.setRank(result.size() + 1);
            rank.setData(dto);
            result.add(rank);
        }
        return JsonResult.of(result);
    }

    @Override
    public List<SimpleBlogDTO> findAllByIds(Collection<String> ids, Map<String, Integer> viewsMap) {
        if (CollectionUtils.isEmpty(ids)) {
            return Lists.newArrayList();
        }
        return redisTemplate.execute((RedisCallback<List<SimpleBlogDTO>>) connection ->
        {
            byte[][] keys = ids.stream().map(BLOG::keyBytes).toArray(byte[][]::new);
            List<byte[]> bytes = connection.mGet(keys);
            if (bytes == null) {
                return Lists.newArrayList();
            }
            return bytes
                    .stream()
                    .map(jsonData ->
                    {
                        BlogData data = JSON.parseObject(IStringUtils.newString(jsonData), BlogData.class);
                        SimpleBlogDTO vo = new SimpleBlogDTO();
                        BeanUtils.copyProperties(data, vo);
                        vo.setViews(viewsMap.getOrDefault(vo.getId(), 0));
                        return vo;
                    })
                    .collect(Collectors.toList());
        });
    }

    @Override
    public JsonResult<List<SimpleBlogDTO>> findAllByCategory(String category) {
        return JsonResult.of(trans2VoList(blogEsRepository.findAllByCategory(category)));
    }

    @Override
    public JsonResult<List<SimpleBlogDTO>> findAllByTags(String tag) {
        return JsonResult.of(trans2VoList(blogEsRepository.findAllByTag(tag)));
    }

    @Override
    public JsonResult<List<BlogSummaryDTO>> search(String queryString) {
        return JsonResult.of(blogEsRepository.search(queryString));
    }

    @Override
    public JsonResult<List<SimpleBlogDTO>> getNewest(Integer number) {
        return JsonResult.of(trans2VoList(blogEsRepository.getNewest(number)));
    }

    private List<SimpleBlogDTO> trans2VoList(List<Blog> blogList) {
        return blogList.stream().map(blog -> {
            SimpleBlogDTO vo = new SimpleBlogDTO();
            vo.setId(blog.getId());
            vo.setIntroduction(blog.getId());
            vo.setTitle(blog.getTitle());
            vo.setIntroduction(blog.getIntroduction());
            vo.setPublishTime(blog.getPublishTime());
            return vo;
        }).collect(Collectors.toList());
    }
}
