package org.jjche.mybatis.base.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jjche.mybatis.base.MyBaseMapper;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;

/**
 * <p>
 * 自定义Service
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-26
 */
public interface IMyService<T> extends IService<T> {

    /**
     * {@inheritDoc}
     *
     * <p>
     * 获取当前mapper
     * </p>
     *
     * @return /
     */
    @Override
    MyBaseMapper<T> getBaseMapper();

    /**
     * 逻辑删除并填充字段
     *
     * @param entity DO
     * @return 影响行数
     */
    default int removeByIdWithFill(T entity) {
        return this.getBaseMapper().deleteByIdWithFill(entity);
    }

    /**
     * 批量逻辑删除并填充字段
     *
     * @param entity  DO
     * @param wrapper 条件构造器
     * @return 影响行数
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    default int removeBatchWithFill(T entity, Wrapper<T> wrapper) {
        return this.getBaseMapper().deleteBatchWithFill(entity, wrapper);
    }

    /**
     * 批量逻辑删除ids并填充字段
     *
     * @param entity DO
     * @param idList 主键
     * @return 影响行数
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    default int removeBatchByIdsWithFill(T entity, Collection<? extends Serializable> idList) {
        return this.getBaseMapper().deleteBatchByIdsWithFill(entity, idList);
    }

    /**
     * <p>
     * 拼成一条insert sql
     * 注意：sql中会有null插入进去，会使表字段not null default value不生效，要手动赋值
     * </p>
     *
     * @param entityList
     * @return /
     */
    @Transactional(
            rollbackFor = {Exception.class}
    )
    default boolean fastSaveBatch(Collection<T> entityList) {
        return this.fastSaveBatch(entityList, DEFAULT_BATCH_SIZE);
    }

    boolean fastSaveBatch(Collection<T> entityList, int batchSize);

}
