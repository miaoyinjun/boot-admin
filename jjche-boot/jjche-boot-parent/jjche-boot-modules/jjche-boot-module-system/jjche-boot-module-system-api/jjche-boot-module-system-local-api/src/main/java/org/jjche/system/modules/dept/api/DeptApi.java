package org.jjche.system.modules.dept.api;

import org.jjche.common.dto.DeptSmallDTO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

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

    /**
     * 根据ID查询
     *
     * @param ids ID
     * @return /
     */
    List<DeptSmallDTO> listByIds(Set<Long> ids);

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return /
     */
    DeptSmallDTO getSmallById(Long id);

    /**
     * <p>
     * 获取所有精简部门
     * </p>
     *
     * @return /
     */
    List<DeptSmallDTO> listSmall();
}
