package com.gapache.oms.store.location.server.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.oms.store.location.sdk.model.enums.StoreType;
import com.gapache.oms.store.location.sdk.model.vo.GeoCodeGeoResponseVO;
import com.gapache.oms.store.location.sdk.model.vo.StoreVO;
import com.gapache.oms.store.location.server.dao.po.StorePO;
import com.gapache.oms.store.location.server.dao.repository.StoreRepository;
import com.gapache.oms.store.location.server.service.GeoService;
import com.gapache.oms.store.location.server.service.StoreService;
import com.gapache.oms.store.location.server.trans.StorePo2Vo;
import com.gapache.oms.store.location.server.trans.StoreVo2Po;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoDistanceQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author HuSen
 * @since 2020/6/24 11:36 上午
 */
@Slf4j
@Service
public class StoreServiceImpl implements StoreService {

    private static final String STORE_INDEX = "store";

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
                builder.field("store_type", store.getStoreType().name());
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
            StorePo2Vo storePo2Vo = new StorePo2Vo();
            // 将地址转化为经纬度
            GeoCodeGeoResponseVO geoResponse = geoService.geocodeGeoResponse(city, address);
            // 先查找5公里范围内的
            NavigableMap<Float, String> stores = findStoreByGeoDistance(5, geoResponse.getLatitude(), geoResponse.getLongitude());
            // 5公里范围内没找到找20公里范围内的
            stores = MapUtils.isEmpty(stores) ? findStoreByGeoDistance(20, geoResponse.getLatitude(), geoResponse.getLongitude()) : stores;
            // 20公里范围内没找到找50公里范围内的
            stores = MapUtils.isEmpty(stores) ? findStoreByGeoDistance(50, geoResponse.getLatitude(), geoResponse.getLongitude()) : stores;
            if (MapUtils.isNotEmpty(stores)) {
                do {
                    Map.Entry<Float, String> entry = stores.pollLastEntry();
                    StorePO store = storeRepository.findByCode(entry.getValue());
                    if (Objects.nonNull(store)) {
                        return JsonResult.of(storePo2Vo.apply(store));
                    }
                } while (!stores.isEmpty());
            }
            // 50公里没有找到找市仓库
            String cityTemp = StringUtils.isNotBlank(city) ? city : geoResponse.getCity();
            StorePO cityWarehouse = storeRepository.findByCityAndStoreType(cityTemp, StoreType.CITY_WAREHOUSE);
            if (Objects.nonNull(cityWarehouse)) {
                return JsonResult.of(storePo2Vo.apply(cityWarehouse));
            }
            // 市仓库没找到找省仓
            StorePO provinceWarehouse = storeRepository.findByProvinceAndStoreType(geoResponse.getProvince(), StoreType.PROVINCE_WAREHOUSE);
            if (Objects.nonNull(provinceWarehouse)) {
                return JsonResult.of(storePo2Vo.apply(provinceWarehouse));
            }
            // 省仓库没找到则找总仓
            List<StorePO> generalWarehouses = storeRepository.findAllByStoreType(StoreType.GENERAL_WAREHOUSE);
            if (CollectionUtils.isNotEmpty(generalWarehouses)) {
                int i = RandomUtils.nextInt(0, generalWarehouses.size());
                return JsonResult.of(storePo2Vo.apply(generalWarehouses.get(i)));
            }
        } catch (Exception e) {
            log.error("查找配送门店发生异常!!!");
        }
        return JsonResult.success();
    }

    private NavigableMap<Float, String> findStoreByGeoDistance(int distance, double lat, double lon) {
        NavigableMap<Float, String> codes = new TreeMap<>();
        try {
            GeoDistanceQueryBuilder queryBuilder = QueryBuilders.geoDistanceQuery("location")
                    .point(lat, lon)
                    .distance(distance, DistanceUnit.KILOMETERS);
            BoolQueryBuilder must = QueryBuilders.boolQuery().must(queryBuilder);
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().query(must);
            SearchRequest searchRequest = new SearchRequest()
                    .indices(STORE_INDEX)
                    .source(searchSourceBuilder);
            SearchResponse response = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            log.info("根据距离查询门店结果:{}", response);

            if (RestStatus.OK.equals(response.status())) {
                SearchHit[] hits = response.getHits().getHits();
                for (SearchHit hit : hits) {
                    String code = hit.field("code").getValue();
                    codes.put(hit.getScore(), code);
                }
            }
        } catch (Exception e) {
            log.error("findStoreByGeoDistance:", e);
        }
        return codes;
    }
}
