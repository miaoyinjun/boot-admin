package org.jjche.system.modules.system.mapper;

import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.system.modules.system.domain.DeptDO;

import java.util.List;

/**
 * <p>UserMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
public interface DeptMapper extends MyBaseMapper<DeptDO> {
    /**
     * 根据角色ID 查询
     *
     * @param roleId 角色ID
     * @return /
     */
    List<DeptDO> selectByRoleId(Long roleId);
}
