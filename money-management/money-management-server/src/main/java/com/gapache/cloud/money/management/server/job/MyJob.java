package com.gapache.cloud.money.management.server.job;

import org.apache.shardingsphere.elasticjob.api.JobConfiguration;
import org.apache.shardingsphere.elasticjob.api.ShardingContext;
import org.apache.shardingsphere.elasticjob.lite.api.bootstrap.impl.ScheduleJobBootstrap;
import org.apache.shardingsphere.elasticjob.reg.base.CoordinatorRegistryCenter;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperConfiguration;
import org.apache.shardingsphere.elasticjob.reg.zookeeper.ZookeeperRegistryCenter;
import org.apache.shardingsphere.elasticjob.simple.job.SimpleJob;

/**
 * @author HuSen
 * @since 2020/8/17 1:24 下午
 */
public class MyJob implements SimpleJob {

    @Override
    public void execute(ShardingContext shardingContext) {
        switch (shardingContext.getShardingItem()) {
            //
            case 0: {
                System.out.println("00000000000000");
                break;
            }
            case 1: {
                System.out.println("11111111111111");
                break;
            }
            case 2: {
                System.out.println("22222222222222");
                break;
            }
            default:
                System.err.println("error");
        }
    }

    public static void main(String[] args) {
        ScheduleJobBootstrap bootstrap = new ScheduleJobBootstrap(createRegistryCenter(), new MyJob(), createJobConfiguration());
        bootstrap.schedule();
    }

    private static CoordinatorRegistryCenter createRegistryCenter() {
        ZookeeperRegistryCenter registryCenter = new ZookeeperRegistryCenter(new ZookeeperConfiguration("118.24.38.46:2181", "my-job"));
        registryCenter.init();
        return registryCenter;
    }

    private static JobConfiguration createJobConfiguration() {
        return JobConfiguration.newBuilder("MyJob", 1).cron("0/5 * * * * ?").build();
    }
}
