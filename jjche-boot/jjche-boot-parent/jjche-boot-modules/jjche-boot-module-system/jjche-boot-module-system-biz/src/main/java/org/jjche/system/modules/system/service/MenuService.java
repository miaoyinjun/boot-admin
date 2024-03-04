package org.jjche.system.modules.system.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.anno.Cached;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.constant.CacheKey;
import org.jjche.common.dto.RoleSmallDto;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.util.ValidationUtil;
import org.jjche.core.util.FileUtil;
import org.jjche.core.util.SecurityUtil;
import org.jjche.mybatis.base.service.MyServiceImpl;
import org.jjche.mybatis.util.MybatisUtil;
import org.jjche.system.modules.system.api.dto.MenuDTO;
import org.jjche.system.modules.system.api.dto.MenuQueryCriteriaDTO;
import org.jjche.system.modules.system.api.vo.MenuMetaVO;
import org.jjche.system.modules.system.api.vo.MenuVO;
import org.jjche.system.modules.system.domain.MenuDO;
import org.jjche.system.modules.system.domain.RoleDO;
import org.jjche.system.modules.system.domain.UserDO;
import org.jjche.system.modules.system.mapper.MenuMapper;
import org.jjche.system.modules.system.mapper.UserMapper;
import org.jjche.system.modules.system.mapstruct.MenuMapStruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>MenuService class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class MenuService extends MyServiceImpl<MenuMapper, MenuDO> {

    private final UserMapper userRepository;
    private final DataPermissionFieldService dataPermissionFieldService;
    private final DataPermissionRuleService dataPermissionRuleService;
    private final MenuMapStruct menuMapStruct;
    private final RoleService roleService;
    private final RedisService redisService;

    /**
     * <p>
     * 获取列表查询语句
     * </p>
     *
     * @param criteria 条件
     * @return sql
     */
    private LambdaQueryWrapper queryWrapper(MenuQueryCriteriaDTO criteria) {
        LambdaQueryWrapper<MenuDO> queryWrapper = MybatisUtil.assemblyLambdaQueryWrapper(criteria);
        String blurry = criteria.getBlurry();
        if (StrUtil.isNotBlank(blurry)) {
            queryWrapper.apply("(title LIKE {0} OR component LIKE {0} OR permission LIKE {0})", "%" + blurry + "%");
        }
        queryWrapper.orderByAsc(MenuDO::getMenuSort);
        return queryWrapper;
    }

    /**
     * 查询全部数据
     *
     * @param criteria 条件
     * @param isQuery  是否查询pid
     * @return /
     * @throws java.lang.IllegalAccessException if any.
     */
    public List<MenuDTO> queryAll(MenuQueryCriteriaDTO criteria, Boolean isQuery) throws IllegalAccessException {
        if (isQuery) {
            criteria.setPidIsNull(true);
            Field[] fields = ClassUtil.getDeclaredFields(criteria.getClass());
            for (Field field : fields) {
                //设置对象的访问权限，保证对private的属性的访问
                field.setAccessible(true);
                Object val = field.get(criteria);
                if ("pidIsNull".equals(field.getName())) {
                    continue;
                }
                if (ObjectUtil.isNotNull(val)) {
                    criteria.setPidIsNull(null);
                    break;
                }
            }
        }
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        return menuMapStruct.toVO(this.list(queryWrapper));
    }

    /**
     * <p>queryPage.</p>
     *
     * @param criteria a {@link MenuQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link PageParam} object.
     */
    public MyPage<MenuDTO> queryPage(MenuQueryCriteriaDTO criteria, PageParam pageable) {
        LambdaQueryWrapper queryWrapper = queryWrapper(criteria);
        MyPage myPage = this.page(pageable, queryWrapper);
        List<MenuDTO> list = menuMapStruct.toVO(myPage.getRecords());
        myPage.setNewRecords(list);
        return myPage;
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    @Cached(name = CacheKey.MENU_ID, key = "#id")
    public MenuDTO findById(long id) {
        MenuDO menu = this.getById(id);
        return menuMapStruct.toVO(menu);
    }

    /**
     * 根据当前用户获取菜单
     *
     * @param currentUserId /
     * @return /
     */
    @Cached(name = CacheKey.MENU_USER_ID, key = "#currentUserId")
    public List<MenuDTO> findByUser(Long currentUserId) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(MenuDO::getType, 2);
        queryWrapper.orderByAsc(MenuDO::getMenuSort);
        //管理员全部权限
        List<MenuDO> menus = null;
        if (SecurityUtil.isAdmin()) {
            menus = this.list(queryWrapper);
        } else {
            List<RoleSmallDto> roles = roleService.findByUsersId(currentUserId);
            Set<Long> roleIds = roles.stream().map(RoleSmallDto::getId).collect(Collectors.toSet());
            if (CollUtil.isNotEmpty(roleIds)) {
                menus = this.baseMapper.findByRoleIdsAndTypeNot(roleIds, 2);
            }
        }
        return this.menuMapStruct.toVO(menus);
    }

    /**
     * <p>
     * 根据权限标识查询
     * </p>
     *
     * @param permission 权限标识
     * @return /
     */
    public MenuDO findByPermission(String permission) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getPermission, permission);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * 根据标题查询
     * </p>
     *
     * @param title 标题
     * @return /
     */
    public MenuDO findByTitle(String title) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getTitle, title);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * 根据组件名查询
     * </p>
     *
     * @param name 组件名
     * @return /
     */
    public MenuDO findByName(String name) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getName, name);
        return this.getOne(queryWrapper);
    }

    /**
     * <p>
     * 根据父id查询
     * </p>
     *
     * @param pId 父id
     * @return /
     */
    public List<MenuDO> findByPid(Long pId) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getPid, pId);
        queryWrapper.orderByAsc(MenuDO::getMenuSort);
        return this.list(queryWrapper);
    }

    /**
     * <p>
     * 查询父id为null
     * </p>
     *
     * @return /
     */
    public List<MenuDO> findByPidIsNull() {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.isNull(MenuDO::getPid);
        queryWrapper.orderByAsc(MenuDO::getMenuSort);
        return this.list(queryWrapper);
    }

    /**
     * <p>
     * 统计父id
     * </p>
     *
     * @param pId 父id
     * @return /
     */
    public Long countByPid(Long pId) {
        LambdaQueryWrapper<MenuDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MenuDO::getPid, pId);
        return this.count(queryWrapper);
    }

    /**
     * 创建
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void create(MenuDO resources) {
        MenuDO menu = this.findByTitle(resources.getTitle());
        Assert.isNull(menu, resources.getTitle() + "已存在");
        String componentName = resources.getName();
        if (StrUtil.isNotBlank(componentName)) {
            MenuDO menu1 = this.findByName(componentName);
            Assert.isNull(menu1, resources.getName() + "已存在");
        }
        if (resources.getPid().equals(0L)) {
            resources.setPid(null);
        }
        if (resources.getIFrame()) {
            String http = "http://", https = "https://";
            Boolean isHttpOrHttps = !(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https));
            Assert.isFalse(isHttpOrHttps, "外链必须以http://或者https://开头");
        }
        this.save(resources);
        // 计算子节点数目
        resources.setSubCount(0);
        // 更新父节点菜单数目
        updateSubCnt(resources.getPid());
    }

    /**
     * 编辑
     *
     * @param resources /
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuDO resources) {
        Boolean isSelf = resources.getId().equals(resources.getPid());
        Assert.isFalse(isSelf, "上级不能为自己");
        MenuDO menu = this.getById(resources.getId());
        ValidationUtil.isNull(menu.getId(), "Permission", "id", resources.getId());

        if (resources.getIFrame()) {
            String http = "http://", https = "https://";
            Boolean isHttpOrHttps = !(resources.getPath().toLowerCase().startsWith(http) || resources.getPath().toLowerCase().startsWith(https));
            Assert.isFalse(isHttpOrHttps, "外链必须以http://或者https://开头");
        }
        MenuDO menu1 = this.findByTitle(resources.getTitle());
        Boolean isMenuEqual = menu1 != null && !menu1.getId().equals(menu.getId());
        Assert.isFalse(isMenuEqual, resources.getTitle() + "已存在");

        if (resources.getPid().equals(0L)) {
            resources.setPid(null);
        }

        // 记录的父节点ID
        Long oldPid = menu.getPid();
        Long newPid = resources.getPid();
        String componentName = resources.getName();
        if (StrUtil.isNotBlank(componentName)) {
            menu1 = this.findByName(componentName);
            Boolean isMenuEqual2 = menu1 != null && !menu1.getId().equals(menu.getId());
            Assert.isFalse(isMenuEqual2, componentName + "已存在");
        }
        menu.setTitle(resources.getTitle());
        menu.setComponent(resources.getComponent());
        menu.setPath(resources.getPath());
        menu.setIcon(resources.getIcon());
        menu.setIFrame(resources.getIFrame());
        menu.setPid(resources.getPid());
        menu.setMenuSort(resources.getMenuSort());
        menu.setCache(resources.getCache());
        menu.setHidden(resources.getHidden());
        menu.setName(resources.getName());
        menu.setPermission(resources.getPermission());
        menu.setType(resources.getType());
        this.updateById(menu);
        // 计算父级菜单节点数目
        updateSubCnt(oldPid);
        updateSubCnt(newPid);
        // 清理缓存
        delCaches(resources.getId());
    }

    /**
     * 获取所有子节点，包含自身ID
     *
     * @param menuList /
     * @param menuSet  /
     * @return /
     */
    public Set<MenuDO> getChildMenus(List<MenuDO> menuList, Set<MenuDO> menuSet) {
        for (MenuDO menu : menuList) {
            menuSet.add(menu);
            List<MenuDO> menus = this.findByPid(menu.getId());
            if (menus != null && menus.size() != 0) {
                getChildMenus(menus, menuSet);
            }
        }
        return menuSet;
    }

    /**
     * 删除
     *
     * @param menuSet /
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(Set<MenuDO> menuSet) {
        for (MenuDO menu : menuSet) {
            // 清理缓存
            delCaches(menu.getId());
            roleService.untiedMenu(menu.getId());
            this.removeById(menu.getId());
            updateSubCnt(menu.getPid());
        }
    }

    /**
     * <p>
     * 懒加载数据
     * </p>
     *
     * @param pid    父id
     * @param roleId 角色id
     * @return /
     */
    public List<MenuDTO> getMenus(Long pid, Long roleId) {
        List<MenuDO> menus;
        if (pid != null && !pid.equals(0L)) {
            menus = this.findByPid(pid);
        } else {
            menus = this.findByPidIsNull();
        }
        List<MenuDTO> list = menuMapStruct.toVO(menus);
        if (roleId != null && roleId > 0 && CollUtil.isNotEmpty(list)) {
            for (MenuDTO menuDto : list) {
                Long menuId = menuDto.getId();
                Long countFiled = dataPermissionFieldService.countByMenuId(menuId);
                Long countRule = dataPermissionRuleService.countByMenuId(menuId);
                Boolean isDataPermission = (countFiled != null && countFiled > 0) || countRule != null && countRule > 0;
                menuDto.setIsDataPermission(isDataPermission);
            }
        }
        return list;
    }

    /**
     * 根据ID获取同级与上级数据
     *
     * @param menuDto /
     * @param menus   /
     * @return /
     */
    public List<MenuDTO> getSuperior(MenuDTO menuDto, List<MenuDO> menus) {
        if (menuDto.getPid() == null) {
            menus.addAll(this.findByPidIsNull());
            return menuMapStruct.toVO(menus);
        }
        menus.addAll(this.findByPid(menuDto.getPid()));
        return getSuperior(findById(menuDto.getPid()), menus);
    }

    /**
     * 构建菜单树
     *
     * @param menuDtos 原始数据
     * @return /
     */
    public List<MenuDTO> buildTree(List<MenuDTO> menuDtos) {
        List<MenuDTO> trees = new ArrayList<>();
        if (CollUtil.isNotEmpty(menuDtos)) {
            Set<Long> ids = new HashSet<>();
            for (MenuDTO menuDTO : menuDtos) {
                if (menuDTO.getPid() == null) {
                    trees.add(menuDTO);
                }
                for (MenuDTO it : menuDtos) {
                    if (menuDTO.getId().equals(it.getPid())) {
                        if (menuDTO.getChildren() == null) {
                            menuDTO.setChildren(new ArrayList<>());
                        }
                        menuDTO.getChildren().add(it);
                        ids.add(it.getId());
                    }
                }
            }
            if (trees.size() == 0) {
                trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
            }
        }
        return trees;
    }

    /**
     * 构建菜单树
     *
     * @param menuDtos /
     * @return /
     */
    public List<MenuVO> buildMenus(List<MenuDTO> menuDtos) {
        List<MenuVO> list = new LinkedList<>();
        menuDtos.forEach(menuDTO -> {
                    if (menuDTO != null) {
                        List<MenuDTO> menuDtoList = menuDTO.getChildren();
                        MenuVO menuVo = new MenuVO();
                        menuVo.setName(ObjectUtil.isNotEmpty(menuDTO.getComponentName()) ? menuDTO.getComponentName() : menuDTO.getTitle());
                        // 一级目录需要加斜杠，不然会报警告
                        menuVo.setPath(menuDTO.getPid() == null ? "/" + menuDTO.getPath() : menuDTO.getPath());
                        menuVo.setHidden(menuDTO.getHidden());
                        // 如果不是外链
                        if (!menuDTO.getIFrame()) {
                            String component = menuDTO.getComponent();
                            if (menuDTO.getPid() == null) {
                                menuVo.setComponent(StrUtil.isEmpty(component) ? "Layout" : component);
                            }else if(menuDTO.getType() == 0){
                                menuVo.setComponent(StrUtil.isEmpty(component)?"ParentView":component);
                            } else if (!StrUtil.isEmpty(component)) {
                                menuVo.setComponent(component);
                            }
                        }
                        menuVo.setMeta(new MenuMetaVO(menuDTO.getTitle(), menuDTO.getIcon(), !menuDTO.getCache()));
                        if (menuDtoList != null && menuDtoList.size() != 0) {
                            menuVo.setAlwaysShow(true);
                            menuVo.setRedirect("noredirect");
                            menuVo.setChildren(buildMenus(menuDtoList));
                            // 处理是一级菜单并且没有子菜单的情况
                        } else if (menuDTO.getPid() == null) {
                            MenuVO menuVo1 = new MenuVO();
                            menuVo1.setMeta(menuVo.getMeta());
                            // 非外链
                            if (!menuDTO.getIFrame()) {
                                menuVo1.setPath("index");
                                menuVo1.setName(menuVo.getName());
                                menuVo1.setComponent(menuVo.getComponent());
                            } else {
                                menuVo1.setPath(menuDTO.getPath());
                            }
                        }
                        list.add(menuVo);
                    }
                }
        );
        return list;
    }

    /**
     * 根据ID查询
     *
     * @param id /
     * @return /
     */
    public MenuDO findOne(Long id) {
        MenuDO menu = this.getById(id);
        ValidationUtil.isNull(menu.getId(), "MenuDO", "id", id);
        return menu;
    }

    /**
     * 导出
     *
     * @param menuDtos 待导出的数据
     * @param response /
     * @throws java.io.IOException if any.
     */
    public void download(List<MenuDTO> menuDtos, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (MenuDTO menuDTO : menuDtos) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("菜单标题", menuDTO.getTitle());
            map.put("菜单类型", NumberUtil.equals(menuDTO.getType(), 0) ? "目录" : NumberUtil.equals(menuDTO.getType(), 1) ? "菜单" : "按钮");
            map.put("权限标识", menuDTO.getPermission());
            map.put("外链菜单", menuDTO.getIFrame() ? "是" : "否");
            map.put("菜单可见", menuDTO.getHidden() ? "否" : "是");
            map.put("是否缓存", menuDTO.getCache() ? "是" : "否");
            map.put("创建日期", menuDTO.getGmtCreate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * <p>
     * 修改菜单子菜单统计
     * </p>
     *
     * @param menuId 菜单id
     */
    private void updateSubCnt(Long menuId) {
        if (menuId != null) {
            Long count = this.countByPid(menuId);
            UpdateWrapper updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("sub_count", count);
            updateWrapper.eq("id", menuId);
            this.update(updateWrapper);
        }
    }

    /**
     * 清理缓存
     *
     * @param id 菜单ID
     */
    public void delCaches(Long id) {
        List<UserDO> users = userRepository.findByMenuId(id);
        redisService.delete(CacheKey.MENU_ID + id);
        redisService.delByKeys(CacheKey.MENU_USER_ID, users.stream().map(UserDO::getId).collect(Collectors.toSet()));
        // 清除 RoleDO 缓存
        List<RoleDO> roles = roleService.findInMenuId(new ArrayList<Long>() {{
            add(id);
        }});
        redisService.delByKeys(CacheKey.ROLE_ID, roles.stream().map(RoleDO::getId).collect(Collectors.toSet()));
    }
}
