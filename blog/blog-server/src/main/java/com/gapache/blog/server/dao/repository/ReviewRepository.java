package com.gapache.blog.server.dao.repository;

import com.gapache.blog.server.dao.entity.ReviewEntity;
import com.gapache.jpa.BaseJpaRepository;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author HuSen
 * @since 2020/8/28 1:39 下午
 */
public interface ReviewRepository extends BaseJpaRepository<ReviewEntity, Long> {

    /**
     * 查询主评论
     *
     * @param pageable 分页参数
     * @return 查询结果
     */
    List<ReviewEntity> findAllByParentIdIsNull(Pageable pageable);
}
