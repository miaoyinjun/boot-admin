package org.jjche.core.base;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.jjche.common.param.MyPage;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

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
public interface BaseVoMapStruct<E, V> {
    /**
     * <p>
     * DO转VO
     * </p>
     *
     * @param doo DO
     * @return VO
     */
    V toVO(E doo);

    /**
     * <p>
     * DO集合转VO集合
     * </p>
     *
     * @param dooList DOList
     * @return VOList
     */
    @Mappings({
            @Mapping(source = "activityId", target = "key"),
            @Mapping(source = "activityType", target = "type")
    })
    List<V> toVO(Collection<E> dooList);

    /**
     * <p>
     * DO分页转VO分页
     * </p>
     *
     * @param pageDo 分页DO
     * @return VO分页
     */
    MyPage<V> toVO(Page<E> pageDo);
}
