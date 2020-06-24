package com.gapache.oms.store.location.server.search;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;

/**
 * @author HuSen
 * @since 2020/6/24 11:03 上午
 */
public class StoreSearchService {

    public void create() throws IOException {
        XContentBuilder jsonBuilder = XContentFactory.jsonBuilder();
        jsonBuilder.startObject();
        {
            // lat:纬度 lon:经度
            jsonBuilder.latlon("s", 1, 1);
        }
        jsonBuilder.endObject();
    }
}
