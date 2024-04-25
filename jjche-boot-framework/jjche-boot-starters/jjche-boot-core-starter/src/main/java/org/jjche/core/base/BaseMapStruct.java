package org.jjche.core.base;

import org.jjche.common.param.MyPage;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * pojo类型转换
 * </p>
 *
 * @author miaoyj
 * @version 1.0.8-SNAPSHOT
 * @since 2020-10-12
 */
public interface BaseMapStruct<E, D, V> extends BaseVoMapStruct<E, V>{

    /**
     * <p>
     * DTO转DO
     * </p>
     *
     * @param dto DTO
     * @return DO
     */
    E toDO(D dto);

    /**
     * <p>
     * DO转DTO
     * </p>
     *
     * @param doo DO
     * @return DTO
     */
    D toDTO(E doo);

    /**
     * <p>
     * DO转DTO
     * </p>
     *
     * @param dooList DO
     * @return DTO
     */
    List<D> toDTO(Collection<E> dooList);

    /**
     * <p>
     * DTO集合转DO集合
     * </p>
     *
     * @param dtoList dtoList
     * @return DOList
     */
    List<E> toDO(Collection<D> dtoList);


}
