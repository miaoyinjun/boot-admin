package org.jjche.mybatis.base;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 自定义baseMapper
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2020-08-26
 */
public interface MyBaseMapper<T> extends BaseMapper<T> {

    /**
     * 逻辑删除并填充字段
     *
     * @param entity DO
     * @return 影响行数
     */
    int deleteByIdWithFill(T entity);

    /**
     * 批量逻辑删除并填充字段
     *
     * @param entity  DO
     * @param wrapper 条件构造器
     * @return 影响行数
     */
    int deleteBatchWithFill(@Param(Constants.ENTITY) T entity, @Param(Constants.WRAPPER) Wrapper<T> wrapper);

    /**
     * 批量逻辑删除ids并填充字段
     *
     * @param entity DO
     * @param idList 主键
     * @return 影响行数
     */
    int deleteBatchByIdsWithFill(@Param(Constants.ENTITY) T entity, @Param("coll") Collection<? extends Serializable> idList);

    /**
     * 全量插入,等价于insert
     *
     * @param entityList
     * @return
     */
    int insertBatchSomeColumn(List<T> entityList);
}
