package org.jjche.sys.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.SecurityConstant;
import org.jjche.common.dto.RoleSmallDTO;
import org.jjche.common.dto.SimpleGrantedAuthorityDTO;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.vo.UserVO;
import org.jjche.core.util.FileUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.security.service.JwtUserService;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.jjche.sys.modules.system.constant.RoleCacheKey;
import org.jjche.sys.modules.system.domain.*;
import org.jjche.sys.modules.system.dto.RoleDTO;
import org.jjche.sys.modules.system.dto.RoleQueryCriteriaDTO;
import org.jjche.sys.modules.system.enums.DataScopeEnum;
import org.jjche.sys.modules.system.mapper.RoleDeptMapper;
import org.jjche.sys.modules.system.mapper.RoleMapper;
import org.jjche.sys.modules.system.mapper.RoleMenuMapper;
import org.jjche.sys.modules.system.mapper.UserMapper;
import org.jjche.sys.modules.system.mapstruct.RoleMapStruct;
import org.jjche.sys.modules.system.mapstruct.RoleSmallMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author miaoyj
 * @since 2022-07-25
 */
@Service
@RequiredArgsConstructor
public class RoleService extends MyServiceImpl<RoleMapper, RoleDO> {

    private final RoleMenuMapper roleMenuMapper;
    private final RoleDeptMapper roleDeptMapper;
    private final RoleMapStruct roleMapper;
    private final RoleSmallMapStruct roleSmallMapper;
    private final RedisService redisService;
    private final UserMapper userMapper;
    private final JwtUserService jwtUserService;
    private final DataPermissionFieldService dataPermissionFieldService;
    private final DataPermissionRuleService dataPermissionRuleService;

    /**
     * 查询全部数据
     *
     * @return /
     */
    public List<RoleDTO> queryAll() {
        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(RoleDO::getLevel);
        return roleMapper.toVO(this.list(queryWrapper));
    }

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(RoleQueryCriteriaDTO criteria) {
        LambdaQueryWrapper<RoleDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(name LIKE {0} OR description LIKE {0})", "%" + blurry + "%");
        }
        queryWrapper.orderByAsc(RoleDO::getLevel);
        return queryWrapper;
    }

    /**
     * 查询全部
     *
     * @param criteria 条件
     * @return /
     */
    public List<RoleDTO> queryAll(RoleQueryCriteriaDTO criteria) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return roleMapper.toVO(this.list(queryWrapper));
    }

    /**
     * 待条件分页查询
     *
     * @param criteria 条件
     * @param page     分页参数
     * @return /
     */
    public MyPage<RoleDTO> queryAll(RoleQueryCriteriaDTO criteria, PageParam page) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage<RoleDO> myPage = this.baseMapper.pageQuery(page, queryWrapper);
        List<RoleDO> roleList = myPage.getRecords();
        if (CollUtil.isNotEmpty(roleList)) {
            for (RoleDO role : roleList) {
                Set<MenuDO> menus = role.getMenus();
                Set<MenuDO> newMenus = CollUtil.newHashSet();
                if (CollUtil.isNotEmpty(menus)) {
                    for (MenuDO menu : menus) {
                        Long menuId = menu.getId();
                        Long countFiled = dataPermissionFieldService.countByMenuId(menuId);
                        Long countRule = dataPermissionRuleService.countByMenuId(menuId);
                        Boolean isDataPermission = (countFiled != null && countFiled > 0) || countRule != null && countRule > 0;
                        menu.setIsDataPermission(isDataPermission);
                        newMenus.add(menu);
                    }
                }
                role.setMenus(newMenus);
            }
        }
        List<RoleDTO> list = roleMapper.toVO(roleList);
        for (RoleDTO roleDTO : list) {
            DataScopeEnum dataScope = roleDTO.getDataScope();
            if (dataScope != null) {
                roleDTO.setDataScopeValue(dataScope.getValue());
            }
        }
        MyPage<RoleDTO> resultPage = new MyPage<>();
        resultPage.setRecords(list);
        resultPage.setPages(myPage.getPages());
        resultPage.setTotal(myPage.getTotal());
        return resultPage;
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    @Cached(name = RoleCacheKey.ROLE_ID, key = "#id")
    public RoleDTO findById(long id) {
        RoleDO role = this.getById(id);
        AssertUtil.isNull(role, SysErrorCodeEnum.RECORD_NOT_FOUND);
        return roleMapper.toVO(role);
    }

    /** {@inheritDoc} */
    /**
     * 创建
     *
     * @param resources /
     */
    public void create(RoleDO resources) {
        RoleDO role = this.findByName(resources.getName());
        AssertUtil.isNull(role, SysErrorCodeEnum.ROLE_NAME_ALREADY_ERROR, resources.getName());
        role = this.findByCode(resources.getCode());
        AssertUtil.isNull(role, SysErrorCodeEnum.ROLE_CODE_ALREADY_ERROR, resources.getCode());
        this.save(resources);
        List<Long> deptIds = resources.getDepts().stream().map(DeptDO::getId).collect(Collectors.toList());
        this.updateRoleAndDept(resources.getId(), deptIds);
    }

    /**
     * <p>
     * 根据名称查询
     * </p>
     *
     * @param name 名称
     * @return /
     */
    public RoleDO findByName(String name) {
        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleDO::getName, name);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * 根据标识查询
     * </p>
     *
     * @param code 标识
     * @return /
     */
    public RoleDO findByCode(String code) {
        LambdaQueryWrapper<RoleDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleDO::getCode, code);
        return this.getOne(queryWrapper);
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(RoleDO resources) {
        RoleDO role = this.getById(resources.getId());
        AssertUtil.isNull(role, SysErrorCodeEnum.RECORD_NOT_FOUND);

        RoleDO role1 = this.findByName(resources.getName());
        Boolean isExist = role1 != null && !role1.getId().equals(role.getId());
        AssertUtil.isFalse(isExist, SysErrorCodeEnum.ROLE_NAME_ALREADY_ERROR, resources.getName());

        role.setName(resources.getName());
        role.setDescription(resources.getDescription());
        role.setDataScope(resources.getDataScope());
        role.setLevel(resources.getLevel());
        this.updateById(role);
        List<Long> deptIds = resources.getDepts().stream().map(DeptDO::getId).collect(Collectors.toList());
        updateRoleAndDept(role.getId(), deptIds);
        // 更新相关缓存
        delCaches(role.getId());
    }

    /**
     * 修改绑定的菜单
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateMenu(RoleDO resources) {
        Long roleId = resources.getId();
        // 更新菜单
        List<Long> menuIds = resources.getMenus().stream().map(MenuDO::getId).collect(Collectors.toList());
        updateRoleAndMenu(roleId, menuIds);
        delCaches(resources.getId());
    }

    /**
     * 解绑菜单
     *
     * @param menuId /
     */
    public void untiedMenu(Long menuId) {
        this.baseMapper.untiedMenu(menuId);
    }

    /**
     * 删除
     *
     * @param ids /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<Long> ids) {
        for (Long id : ids) {
            // 更新相关缓存
            delCaches(id);
        }
        this.removeByIds(ids);
    }

    /**
     * 根据用户ID查询
     *
     * @param id 用户ID
     * @return /
     */
    public List<RoleSmallDTO> findByUsersId(Long id) {
        return roleSmallMapper.toVO(this.baseMapper.selectByUserId(id));
    }

    /**
     * 获取用户权限信息
     *
     * @param user 用户信息
     * @return 权限信息
     */
    public List<SimpleGrantedAuthorityDTO> mapToGrantedAuthorities(UserVO user) {
        Set<String> permissions = new HashSet<>();
        // 如果是管理员直接返回
        if (user.getIsAdmin()) {
            permissions.add(SecurityConstant.ADMIN_PERMISSION);
            return permissions.stream().map(SimpleGrantedAuthorityDTO::new).collect(Collectors.toList());
        }
        List<RoleDO> roles = this.baseMapper.selectByUserId(user.getId());
        permissions = roles.stream().flatMap(role -> role.getMenus().stream()).filter(menu -> StrUtil.isNotBlank(menu.getPermission())).map(MenuDO::getPermission).collect(Collectors.toSet());
        return permissions.stream().map(SimpleGrantedAuthorityDTO::new).collect(Collectors.toList());
    }

    /**
     * 导出数据
     *
     * @param roles    待导出的数据
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<RoleDTO> roles, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (RoleDTO role : roles) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("角色名称", role.getName());
            map.put("角色级别", role.getLevel());
            map.put("描述", role.getDescription());
            map.put("创建日期", role.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 验证是否被用户关联
     *
     * @param ids /
     */
    public void verification(Set<Long> ids) {
        AssertUtil.isFalse(userMapper.countByRoles(ids) > 0, SysErrorCodeEnum.ROLE_USER_NOT_ALLOWED_DEL_ERROR);
    }

    /**
     * 根据菜单Id查询
     *
     * @param menuIds /
     * @return /
     */
    public List<RoleDO> findInMenuId(List<Long> menuIds) {
        return this.baseMapper.findInMenuId(menuIds);
    }

    /**
     * 清理缓存
     *
     * @param id /
     */
    public void delCaches(Long id) {
        List<UserDO> users = userMapper.findByRoleId(id);
        if (CollectionUtil.isNotEmpty(users)) {
            users.forEach(item -> jwtUserService.removeByUserName(item.getUsername()));
        }
        redisService.delete(RoleCacheKey.ROLE_ID + id);
    }

    /**
     * <p>
     * 重置角色与部门关系
     * </p>
     *
     * @param roleId  角色id
     * @param deptIds 部门id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAndDept(Long roleId, List<Long> deptIds) {
        LambdaQueryWrapper<RoleDeptDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleDeptDO::getRoleId, roleId);
        this.roleDeptMapper.delete(queryWrapper);
        if (CollUtil.isNotEmpty(deptIds)) {
            for (Long deptId : deptIds) {
                RoleDeptDO roleDeptDO = new RoleDeptDO(roleId, deptId);
                this.roleDeptMapper.insert(roleDeptDO);
            }
        }
    }

    /**
     * <p>
     * 重置角色与菜单关系
     * </p>
     *
     * @param roleId  角色id
     * @param menuIds 菜单id
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateRoleAndMenu(Long roleId, List<Long> menuIds) {
        MenuService menuService = SpringUtil.getBean(MenuService.class);
        LambdaQueryWrapper<RoleMenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenuDO::getRoleId, roleId);
        this.roleMenuMapper.delete(queryWrapper);
        if (CollUtil.isNotEmpty(menuIds)) {
            for (Long menuId : menuIds) {
                RoleMenuDO roleMenuDO = new RoleMenuDO(roleId, menuId);
                this.roleMenuMapper.insert(roleMenuDO);
                menuService.delCaches(menuId);
            }
        }
    }

    /**
     * <p>
     * 根据角色查询角色级别
     * </p>
     *
     * @param roleIds /
     * @return /
     */
    public Integer findByRoles(List<Long> roleIds) {
        if (roleIds.size() == 0) {
            return Integer.MAX_VALUE;
        }
        Set<RoleDTO> roleDtos = new HashSet<>();
        for (Long roleId : roleIds) {
            roleDtos.add(findById(roleId));
        }
        return Collections.min(roleDtos.stream().map(RoleDTO::getLevel).collect(Collectors.toList()));
    }

    /**
     * <p>
     * 获取角色精简信息列表
     * </p>
     *
    * @return /
     */
    public List<RoleSmallDTO> getSimpleRoleList(){
        return roleSmallMapper.toVO(this.list());
    }

    /**
     * 校验角色们是否有效。如下情况，视为无效：
     * 1. 角色编号不存在
     * 2. 角色被禁用
     *
     * @param ids 角色编号数组
     */
    public void validRoleList(Set<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得角色信息
        List<RoleDO> roles = this.listByIds(ids);
        Map<Long, RoleDO> roleMap = MapUtil.newHashMap();
        roleMap = CollUtil.toMap(roles, roleMap, RoleDO::getId);
        // 校验
        Map<Long, RoleDO> finalRoleMap = roleMap;
        ids.forEach(id -> {
            RoleDO role = finalRoleMap.get(id);
            AssertUtil.notNull(role, SysErrorCodeEnum.ROLE_NOT_FOUND_ERROR);
        });
    }

    /**
     * 获得拥有多个角色的用户编号集合
     *
     * @param roleIds 角色编号集合
     * @return 用户编号集合
     */
    public Set<Long> getUserRoleIdByRoleIds(Set<Long> roleIds) {
        UserRoleService userRoleService = SpringUtil.getBean(UserRoleService.class);
        return userRoleService.getUserRoleIdListByRoleIds(roleIds);
    }
}
