package com.gapache.oms.store.location.sdk.model.enums;

import lombok.Getter;

/**
 * @author HuSen
 * @since 2020/6/28 10:38 上午
 */
@Getter
public enum StoreType {
    //
    ORDINARY("普通门店"),
    CITY_WAREHOUSE("市仓"),
    PROVINCE_WAREHOUSE("省仓"),
    GENERAL_WAREHOUSE("总仓");

    private String description;

    StoreType(String description) {
        this.description = description;
    }
}
