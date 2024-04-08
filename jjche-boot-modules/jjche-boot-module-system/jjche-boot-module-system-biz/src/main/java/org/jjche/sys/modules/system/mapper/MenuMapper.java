package org.jjche.sys.modules.system.mapper;

import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.domain.MenuDO;

import java.util.List;
import java.util.Set;

/**
 * <p>JobRepository interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-29
 */
public interface MenuMapper extends MyBaseMapper<MenuDO> {
    /**
     * <p>
     * 根据角色id查询
     * </p>
     *
     * @param roleId 角色id
     * @return /
     */
    List<MenuDO> selectByRoleId(Long roleId);

    /**
     * <p>
     * 查询角色id，类型非
     * </p>
     *
     * @param roleIds 角色id
     * @param type    类型
     * @return /
     */
    List<MenuDO> findByRoleIdsAndTypeNot(Set<Long> roleIds, int type);
}
