package org.jjche.sys.modules.system.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.MyBaseMapper;
import org.jjche.sys.modules.system.domain.RoleDO;

import java.util.List;
import java.util.Set;

/**
 * <p>UserMapStruct interface.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-22
 */
public interface RoleMapper extends MyBaseMapper<RoleDO> {
    /**
     * <p>
     * 根据用户id查找
     * </p>
     *
     * @param userId 用户id
     * @return 角色
     */
    List<RoleDO> selectByUserId(Long userId);


    /**
     * 根据部门查询
     *
     * @param deptIds /
     * @return /
     */
    int countByDepts(Set<Long> deptIds);

    /**
     * 根据菜单Id查询
     *
     * @param menuIds /
     * @return /
     */
    List<RoleDO> findInMenuId(List<Long> menuIds);

    /**
     * 解绑菜单
     *
     * @param menuId /
     */
    void untiedMenu(Long menuId);


    /**
     * <p>
     * 分页查询
     * </p>
     *
     * @param page    分页
     * @param wrapper 自定义sql
     * @return 分页VO
     */
    MyPage<RoleDO> pageQuery(PageParam page, @Param(Constants.WRAPPER) Wrapper wrapper);

}
