package com.gapache.oms.store.location.server.service.impl;

import com.gapache.commons.model.JsonResult;
import com.gapache.commons.utils.HttpUtils;
import com.gapache.oms.store.location.sdk.model.vo.StoreVO;
import com.gapache.oms.store.location.server.dao.po.StorePO;
import com.gapache.oms.store.location.server.dao.repository.StoreRepository;
import com.gapache.oms.store.location.server.service.StoreService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author HuSen
 * @since 2020/6/24 11:36 上午
 */
@Slf4j
@Service
public class StoreServiceImpl implements StoreService {

    private static final String IMAP_KEY = "709777529e5b70b81914a0bbc72b7000";
    private static final String IMAP_SIGN = "33d8fd49df1c56d9bedf69e2539bce3b";
    private static final String GEOCODE_GEO_URL = "https://restapi.amap.com/v3/geocode/geo";

    private static final String STORE_INDEX = "store";

    @Resource
    private RestHighLevelClient restHighLevelClient;

    @Resource
    private StoreRepository storeRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonResult<StoreVO> create(StoreVO store) {
        StorePO po = new StorePO();
        po.setCode(store.getCode());
        po.setName(store.getName());
        po.setProvince(store.getProvince());
        po.setCity(store.getCity());
        po.setArea(store.getArea());
        po.setAddress(store.getAddress());
        po.setLatitude(store.getLatitude());
        po.setLongitude(store.getLongitude());
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
}
