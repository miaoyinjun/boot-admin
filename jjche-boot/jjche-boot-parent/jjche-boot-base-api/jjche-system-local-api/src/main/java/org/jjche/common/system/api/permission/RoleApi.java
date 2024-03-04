package org.jjche.common.system.api.permission;

import java.util.Collection;
import java.util.Set;

/**
 * <p>
 * 角色 API 接口
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-27
 */
public interface RoleApi {

    /**
     * 校验角色们是否有效。如下情况，视为无效：
     * 1. 角色编号不存在
     * 2. 角色被禁用
     *
     * @param ids 角色编号数组
     */
    void validRoleList(Collection<Long> ids);

    /**
     * 获得拥有多个角色的用户编号集合
     *
     * @param roleIds 角色编号集合
     * @return 用户编号集合
     */
    Set<Long> getUserRoleIdListByRoleIds(Collection<Long> roleIds);
}
