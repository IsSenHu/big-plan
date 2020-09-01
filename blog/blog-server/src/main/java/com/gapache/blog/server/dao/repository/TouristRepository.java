package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.entity.TouristEntity;
import com.gapache.jpa.BaseJpaRepository;

/**
 * @author HuSen
 * @since 2020/8/28 1:37 下午
 */
public interface TouristRepository extends BaseJpaRepository<TouristEntity, Long> {

    /**
     * 通过邮箱查找游客
     *
     * @param email 邮箱
     * @return 游客
     */
    TouristEntity findByEmail(String email);

    /**
     * 该昵称是否已经存在
     *
     * @param nick 昵称
     * @return 是否已经存在
     */
    boolean existsByNick(String nick);

    /**
     * 该邮箱是否已经存在
     *
     * @param email 邮箱
     * @return 是否已经存在
     */
    boolean existsByEmail(String email);
}
