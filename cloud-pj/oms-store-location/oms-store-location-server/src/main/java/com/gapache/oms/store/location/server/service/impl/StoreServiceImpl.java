package com.gapache.oms.store.location.server.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.vo.StoreVO;
import com.gapache.oms.store.location.server.dao.po.StorePO;
import com.gapache.oms.store.location.server.dao.repository.StoreRepository;
import com.gapache.oms.store.location.server.service.GeoService;
import com.gapache.oms.store.location.server.service.StoreService;
import com.gapache.oms.store.location.server.trans.StorePo2Vo;
import com.gapache.oms.store.location.server.trans.StoreVo2Po;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.document.DocumentField;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author HuSen
 * @since 2020/6/24 11:36 上午
 */
@Slf4j
@Service
public class StoreServiceImpl implements StoreService {

    private static final String STORE_INDEX = "store";
    private static final String STORE_DELIVER_RECORD_INDEX = "storeDeliverRecord";

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private StoreRepository storeRepository;

    @Resource
    private GeoService geoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<StoreVO> create(StoreVO store) {
        StorePO po = new StoreVo2Po().apply(store);
        storeRepository.save(po);
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("code", store.getCode());
                builder.field("name", store.getName());
                builder.field("province", store.getProvince());
                builder.field("city", store.getCity());
                builder.field("area", store.getArea());
                builder.field("address", store.getAddress());
                builder.latlon("location", store.getLatitude(), store.getLongitude());
            }
            builder.endObject();

            IndexRequest indexRequest = new IndexRequest()
                    .index(STORE_INDEX)
                    .id(store.getCode())
                    .source(builder);

            IndexResponse response = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            if (log.isDebugEnabled()) {
                log.debug("index:{}", response);
            }
        } catch (Exception e) {
            log.error("创建门店发生异常:{}", store, e);
        }
        return JsonResult.of(store);
    }

    @Override
    public JsonResult<StoreVO> findClosestDistanceByAddress(String city, String address) {
        try {
            // 首先去查找这个地址有没有门店配送的记录，如果有则直接返回
            GetRequest getRequest = new GetRequest()
                    .index(STORE_DELIVER_RECORD_INDEX)
                    .id(address);
            GetResponse findStoreDeliverRecordResp = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
            if (findStoreDeliverRecordResp.isExists()) {
                DocumentField storeNo = findStoreDeliverRecordResp.getField("storeNo");
                String code = storeNo.getValue();
                if (log.isInfoEnabled()) {
                    log.info("查询到配送过同一地址的门店编码为:{}", code);
                }
                StorePO store = storeRepository.findByCode(code);
                if (Objects.nonNull(store)) {
                    return JsonResult.of(new StorePo2Vo().apply(store));
                }
            }

            // 然后将地址转化为经纬度，左边经度，右边纬度
            Pair<Double, Double> location = geoService.geocodeGeoOnlyLatLon(city, address);
            // 先查找5公里范围内的
            GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("findClosestDistanceByAddress")
                    .distance(5, DistanceUnit.KILOMETERS)
                    .point(location.getSecond(), location.getFirst());

            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.query(queryBuilder);

            SearchRequest searchRequest = new SearchRequest()
                    .indices(STORE_INDEX)
                    .source(searchSourceBuilder);
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            // 5公里范围内没找到找
        } catch (Exception e) {
            log.error("查找配送门店发生异常!!!");
        }
        return null;
    }
}
