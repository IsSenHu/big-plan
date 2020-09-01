package com.gapache.cloud.money.management.server.job;

import com.gapache.cloud.money.management.common.model.TransactionDTO;
import com.gapache.cloud.money.management.server.service.TransactionService;
import com.gapache.commons.loader.DataLoader;
import lombok.extern.slf4j.Slf4j;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.dataflow.job.DataflowJob;

import java.util.ArrayList;
import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/19 1:23 下午
 */
@Slf4j
public class ExportDataJob implements DataflowJob<TransactionDTO> {

    private final List<DataLoader<TransactionDTO>> dataLoaders;
    private final TransactionService transactionService;

    public ExportDataJob(List<DataLoader<TransactionDTO>> dataLoaders, TransactionService transactionService) {
        this.dataLoaders = dataLoaders;
        this.transactionService = transactionService;
    }

    @Override
    public List<TransactionDTO> fetchData(ShardingContext shardingContext) {
        int shardingItem = shardingContext.getShardingItem();
        log.info("fetchData shardingItem:{}", shardingItem);
        int i = shardingItem % dataLoaders.size();
        DataLoader<TransactionDTO> dataLoader = dataLoaders.get(i);
        try {
            return dataLoader.loading();
        } catch (Exception e) {
            log.error("fetchData error shardingItem:{}, dataLoader:{} .", shardingItem, dataLoader, e);
        }
        return new ArrayList<>(0);
    }

    @Override
    public void processData(ShardingContext shardingContext, List<TransactionDTO> list) {
        int shardingItem = shardingContext.getShardingItem();
        log.info("processData shardingItem:{}", shardingItem);
        transactionService.save(list);
    }
}
