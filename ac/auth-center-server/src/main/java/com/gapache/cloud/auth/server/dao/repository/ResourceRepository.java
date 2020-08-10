package com.gapache.cloud.auth.server.dao.repository;

import com.gapache.cloud.auth.server.dao.entity.ResourceEntity;
import com.gapache.jpa.BaseJpaRepository;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/6 6:03 下午
 */
public interface ResourceRepository extends BaseJpaRepository<ResourceEntity, Long> {

    /**
     * 根据资源服务名删除所有
     *
     * @param resourceServerName 资源服务名
     */
    void deleteByResourceServerName(String resourceServerName);

    /**
     * 根据资源服务名查询所有
     *
     * @param resourceServerName 资源服务名
     * @return 资源
     */
    List<ResourceEntity> findAllByResourceServerName(String resourceServerName);
}
