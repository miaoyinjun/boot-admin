package org.jjche.common.system.api.dept;

import java.util.Collection;

/**
 * <p>
 * 部门 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
public interface DeptApi {

    /**
     * 校验部门们是否有效。如下情况，视为无效：
     * 1. 部门编号不存在
     * 2. 部门被禁用
     *
     * @param ids 角色编号数组
     */
    void validateDeptList(Collection<Long> ids);

}
