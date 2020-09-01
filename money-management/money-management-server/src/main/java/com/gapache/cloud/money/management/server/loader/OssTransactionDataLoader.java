package com.gapache.cloud.money.management.server.loader;

import com.gapache.cloud.money.management.common.model.TransactionDTO;
import com.gapache.commons.loader.DataLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/19 1:30 下午
 */
public class OssTransactionDataLoader implements DataLoader<TransactionDTO> {

    @Override
    public List<TransactionDTO> loading() {
        return new ArrayList<>();
    }
}
