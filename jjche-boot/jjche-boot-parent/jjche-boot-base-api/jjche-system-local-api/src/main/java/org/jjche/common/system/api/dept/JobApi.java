package org.jjche.common.system.api.dept;

import java.util.Collection;

/**
 * <p>
 * 岗位 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
public interface JobApi {

    /**
     * 校验岗位们是否有效。如下情况，视为无效：
     * 1. 岗位编号不存在
     * 2. 岗位被禁用
     *
     * @param ids 岗位编号数组
     */
    void validPostList(Collection<Long> ids);

}
