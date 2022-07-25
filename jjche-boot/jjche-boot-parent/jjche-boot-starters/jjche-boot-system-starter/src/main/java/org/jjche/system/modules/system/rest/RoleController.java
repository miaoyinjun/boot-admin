package org.jjche.system.modules.system.rest;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.annotation.PermissionData;
import org.jjche.common.dto.RoleSmallDto;
import org.jjche.common.dto.UserVO;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.pojo.DataScope;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.util.SecurityUtil;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.system.modules.system.api.dto.RoleDTO;
import org.jjche.system.modules.system.api.dto.RoleQueryCriteriaDTO;
import org.jjche.system.modules.system.api.dto.UserRoleDTO;
import org.jjche.system.modules.system.domain.RoleDO;
import org.jjche.system.modules.system.service.RoleService;
import org.jjche.system.modules.system.service.UserRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>RoleController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-03
 */
@RequiredArgsConstructor
@Api(tags = "系统：角色管理")
@SysRestController("roles")
public class RoleController extends BaseController {

    private static final String ENTITY_NAME = "role";
    private final RoleService roleService;
    private final UserRoleService userRoleService;

    /**
     * <p>query.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("获取单个role")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public ResultWrapper<RoleDTO> query(@PathVariable Long id) {
        return ResultWrapper.ok(roleService.findById(id));
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link RoleQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出角色数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('role:list')")
    public void download(HttpServletResponse response, RoleQueryCriteriaDTO criteria) throws IOException {
        roleService.download(roleService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("返回全部的角色")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public ResultWrapper<List<RoleDTO>> query() {
        return ResultWrapper.ok(roleService.queryAll());
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link RoleQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("查询角色")
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    public ResultWrapper<MyPage<RoleDTO>> query(RoleQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(roleService.queryAll(criteria, pageable));
    }

    /**
     * <p>getLevel.</p>
     *
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("获取用户级别")
    @GetMapping(value = "/level")
    public ResultWrapper<Object> getLevel() {
        return ResultWrapper.ok(Dict.create().set("level", getLevels(null)));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link RoleDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(value = "新增", category = LogCategoryType.MANAGER, type = LogType.ADD, module = "角色")
    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    public ResultWrapper create(@Validated @RequestBody RoleDO resources) {
        Assert.isNull(resources.getId(), "A new " + ENTITY_NAME + " cannot already have an ID");
        getLevels(resources.getLevel());
        roleService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link RoleDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(value = "修改", category = LogCategoryType.MANAGER, type = LogType.UPDATE, module = "角色")
    @ApiOperation("修改角色")
    @PutMapping
    @PreAuthorize("@el.check('roles:edit')")
    public ResultWrapper update(@Validated @RequestBody RoleDO resources) {
        getLevels(resources.getLevel());
        roleService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>updateMenu.</p>
     *
     * @param resources a {@link RoleDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(value = "修改角色菜单", category = LogCategoryType.MANAGER, type = LogType.UPDATE, module = "角色")
    @ApiOperation("修改角色菜单")
    @PutMapping(value = "/menu")
    @PreAuthorize("@el.check('roles:edit')")
    public ResultWrapper updateMenu(@RequestBody RoleDO resources) {
        RoleDTO role = roleService.findById(resources.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecord(value = "删除", category = LogCategoryType.MANAGER, type = LogType.DELETE, module = "角色")
    @ApiOperation("删除角色")
    @DeleteMapping
    @PreAuthorize("@el.check('roles:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            RoleDTO role = roleService.findById(id);
            getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.delete(ids);
        return ResultWrapper.ok();
    }

    /**
     * 获取用户的角色级别
     *
     * @return /
     */
    private int getLevels(Integer level) {
        List<Integer> levels = roleService.findByUsersId(SecurityUtil.getUserId()).stream().map(RoleSmallDto::getLevel).collect(Collectors.toList());
        int min = Collections.min(levels);
        if (level != null) {
            Assert.isFalse(level < min, "权限不足，你的角色级别：" + min + "，低于操作的角色级别：" + level);
        }
        return min;
    }

    /**
     * <p>query.</p>
     *
     * @param roleId   角色id.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("查询下属用户")
    @GetMapping("/users")
    @PreAuthorize("@el.check('roles:edit')")
    @PermissionData(deptIdInFieldName = DataScope.F_SQL_SCOPE_NAME)
    public ResultWrapper<MyPage<UserVO>> listByRole(PageParam pageable, @RequestParam Long roleId, @RequestParam(required = false) String blurry) {
        return ResultWrapper.ok(userRoleService.listUserById(pageable, roleId, blurry));
    }

    /**
     * <p>query.</p>
     *
     * @param roleId   角色id.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation("查询非下属用户")
    @GetMapping("/not/users")
    @PreAuthorize("@el.check('roles:edit')")
    @PermissionData(deptIdInFieldName = DataScope.F_SQL_SCOPE_NAME)
    public ResultWrapper<MyPage<UserVO>> listNotByRole(PageParam pageable, @RequestParam Long roleId, @RequestParam(required = false) String blurry) {
        return ResultWrapper.ok(userRoleService.listNotUserById(pageable, roleId, blurry));
    }

    @LogRecord(value = "删除下属用户", category = LogCategoryType.MANAGER, type = LogType.DELETE, module = "角色")
    @ApiOperation("删除下属用户")
    @DeleteMapping("/users")
    @PreAuthorize("@el.check('roles:del')")
    public ResultWrapper deleteUsers(@Valid @RequestBody List<UserRoleDTO> list) {
        userRoleService.deleteUsers(list);
        return ResultWrapper.ok();
    }

    @LogRecord(value = "添加下属用户", category = LogCategoryType.MANAGER, type = LogType.ADD, module = "角色")
    @ApiOperation("添加下属用户")
    @PostMapping("/users")
    @PreAuthorize("@el.check('roles:del')")
    public ResultWrapper addUsers(@Valid @RequestBody List<UserRoleDTO> list) {
        userRoleService.addUsers(list);
        return ResultWrapper.ok();
    }
}
