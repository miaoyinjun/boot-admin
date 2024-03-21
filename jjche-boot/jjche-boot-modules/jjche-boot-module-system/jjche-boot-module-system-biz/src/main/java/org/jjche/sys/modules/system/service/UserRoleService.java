package org.jjche.sys.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.UserVO;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.sys.modules.system.api.dto.UserRoleDTO;
import org.jjche.sys.modules.system.domain.UserRoleDO;
import org.jjche.sys.modules.system.mapper.UserMapper;
import org.jjche.sys.modules.system.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * <p>
 * 用户角色关系
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-25
 */
@Service
@RequiredArgsConstructor
public class UserRoleService extends MyServiceImpl<UserRoleMapper, UserRoleDO> {

    private final UserRoleMapper userRoleMapper;
    private final UserMapper userMapper;
    private final RoleService roleService;

    /**
     * <p>
     * 查询用户根据角色id
     * </p>
     *
     * @param page   分页
     * @param roleId 角色id
     * @return /
     */
    public MyPage<UserVO> listUserById(PageParam page, Long roleId, String blurry) {
        return userMapper.selectByRoleId(page, roleId, blurry);
    }

    /**
     * <p>
     * 查询用户根据角色id
     * </p>
     *
     * @param page   分页
     * @param roleId 角色id
     * @param blurry 模糊
     * @return /
     */
    public MyPage<UserVO> listNotUserById(PageParam page, Long roleId, String blurry) {
        return userMapper.selectNotByRoleId(page, roleId, blurry);
    }

    /**
     * <p>
     * 删除角色下的用户
     * </p>
     *
     * @param list /
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(List<UserRoleDTO> list) {
        for (UserRoleDTO roleDTO : list) {
            Long roleId = roleDTO.getRoleId();
            Long userId = roleDTO.getUserId();
            LambdaQueryWrapper<UserRoleDO> wrapper = Wrappers.lambdaQuery();
            wrapper.eq(UserRoleDO::getRoleId, roleId);
            wrapper.eq(UserRoleDO::getUserId, userId);
            roleService.delCaches(roleId);
            this.userRoleMapper.delete(wrapper);
        }
    }

    /**
     * <p>
     * 添加角色下的用户
     * </p>
     *
     * @param list /
     */
    @Transactional(rollbackFor = Exception.class)
    public void addUsers(List<UserRoleDTO> list) {
        for (UserRoleDTO userRoleDTO : list) {
            Long roleId = userRoleDTO.getRoleId();
            UserRoleDO userRoleDO = new UserRoleDO(userRoleDTO.getUserId(), roleId);
            this.save(userRoleDO);
            roleService.delCaches(roleId);
        }
    }

    public Set<Long> getUserRoleIdListByRoleIds(Set<Long> roleIds) {
        LambdaQueryWrapper<UserRoleDO> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(UserRoleDO::getUserId);
        queryWrapper.in(UserRoleDO::getRoleId, roleIds);
        return CollUtil.newHashSet(this.listObjs(queryWrapper, a -> {
            return Long.valueOf(a.toString());
        }));
    }
}
