package com.gapache.oms.store.location.server.sdk;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gapache.commons.sdk.AbstractSdk;
import com.gapache.commons.utils.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author HuSen
 * @since 2020/6/24 3:30 下午
 */
@Slf4j
public class IMapSdk extends AbstractSdk {

    private String baseUri;
    private String key;
    private String sig;

    public IMapSdk(String paramPath) {
        super(paramPath);
    }

    @Override
    protected void doReloadLogic(String config) {
        if (log.isInfoEnabled()) {
            log.info("doReloadLogic:{}", config);
        }
        JSONObject configObject = JSON.parseObject(config);
        this.key = configObject.getString("key");
        this.sig = configObject.getString("sig");
        this.baseUri = configObject.getString("baseUri");
    }

    @Override
    protected void doInitLogic(String config) {
        if (log.isInfoEnabled()) {
            log.info("doInitLogic:{}", config);
        }
        JSONObject configObject = JSON.parseObject(config);
        this.key = configObject.getString("key");
        this.sig = configObject.getString("sig");
        this.baseUri = configObject.getString("baseUri");
    }

    public JSONObject geocodeGeo(String city, String address) {
        Map<String, String> params = new HashMap<>(4);
        params.put("key", this.key);
        if (StringUtils.isNotBlank(city)) {
            params.put("city", city);
        }
        params.put("address", address);
        params.put("sig", this.sig);
        String resp = HttpUtils.getSync(baseUri + "/v3/geocode/geo", params, null);
        if (StringUtils.isNotBlank(resp)) {
            return JSONObject.parseObject(resp);
        }
        return null;
    }
}
