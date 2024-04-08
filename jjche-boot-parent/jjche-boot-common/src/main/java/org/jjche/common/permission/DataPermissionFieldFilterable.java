package org.jjche.common.permission;

import java.util.function.Function;

/**
 * <p>
 * 支持过滤
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-11
 */
public interface DataPermissionFieldFilterable<T> {

    /**
     * 遍历列表，执行过滤方法
     *
     * @param filterFunc 过滤方法
     */
    void doFilter(Function<T, T> filterFunc);

    /**
     * <p>
     * 获取数据
     * </p>
     *
     * @return /
     */
    Iterable<T> getData();
}
