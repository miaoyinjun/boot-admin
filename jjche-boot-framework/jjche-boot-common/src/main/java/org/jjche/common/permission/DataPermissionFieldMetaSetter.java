package org.jjche.common.permission;

import org.jjche.common.vo.DataPermissionFieldResultVO;

import java.util.List;

/**
 * <p>
 * 设置数据结构
 * </p>
 *
 * @author miaoyj
 * @version 1.0.10-SNAPSHOT
 * @since 2020-11-11
 */
public interface DataPermissionFieldMetaSetter {

    /**
     * 获取数据结构
     *
     * @return 数据结构
     */
    List<DataPermissionFieldResultVO> getMeta();

    /**
     * 设置数据结构，用于前台展示
     *
     * @param dataResources 数据结构
     */
    void setMeta(List<DataPermissionFieldResultVO> dataResources);

    /**
     * <p>
     * 获取数据
     * </p>
     *
     * @return /
     */
    Iterable getData();
}
