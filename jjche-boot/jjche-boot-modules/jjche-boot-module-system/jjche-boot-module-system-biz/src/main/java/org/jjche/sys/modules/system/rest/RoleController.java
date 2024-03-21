package org.jjche.sys.modules.system.rest;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.annotation.PermissionData;
import org.jjche.common.dto.RoleSmallDTO;
import org.jjche.common.dto.UserVO;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.vo.DataScopeVO;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.core.util.SecurityUtil;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.sys.modules.system.api.dto.RoleDTO;
import org.jjche.sys.modules.system.api.dto.RoleQueryCriteriaDTO;
import org.jjche.sys.modules.system.api.dto.UserRoleDTO;
import org.jjche.sys.modules.system.domain.RoleDO;
import org.jjche.sys.modules.system.service.RoleService;
import org.jjche.sys.modules.system.service.UserRoleService;
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
 * <p>
 * 角色-控制器
 * </p>
 *
 * @author miaoyj
 * @since 2024-02-28
 */
@Api(tags = "系统：角色管理")
@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController extends BaseController {

    private static final String ENTITY_NAME = "role";
    private final RoleService roleService;
    private final UserRoleService userRoleService;

    /**
     * <p>query.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @ApiOperation("获取单个role")
    @GetMapping(value = "/{id}")
    @PreAuthorize("@el.check('roles:list')")
    public R<RoleDTO> query(@PathVariable Long id) {
        return R.ok(roleService.findById(id));
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
     * @return a {@link R} object.
     */
    @ApiOperation("返回全部的角色")
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('roles:list','user:add','user:edit')")
    public R<List<RoleDTO>> query() {
        return R.ok(roleService.queryAll());
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link RoleQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询角色")
    @GetMapping
    @PreAuthorize("@el.check('roles:list')")
    public R<MyPage<RoleDTO>> query(RoleQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(roleService.queryAll(criteria, pageable));
    }

    /**
     * <p>getLevel.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("获取用户级别")
    @GetMapping(value = "/level")
    public R<Object> getLevel() {
        return R.ok(Dict.create().set("level", getLevels(null)));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link RoleDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(value = "新增", category = LogCategoryType.MANAGER, type = LogType.ADD, module = "角色")
    @ApiOperation("新增角色")
    @PostMapping
    @PreAuthorize("@el.check('roles:add')")
    public R create(@Validated @RequestBody RoleDO resources) {
        Assert.isNull(resources.getId(), "A new " + ENTITY_NAME + " cannot already have an ID");
        getLevels(resources.getLevel());
        roleService.create(resources);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link RoleDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(value = "修改", category = LogCategoryType.MANAGER, type = LogType.UPDATE, module = "角色")
    @ApiOperation("修改角色")
    @PutMapping
    @PreAuthorize("@el.check('roles:edit')")
    public R update(@Validated @RequestBody RoleDO resources) {
        getLevels(resources.getLevel());
        roleService.update(resources);
        return R.ok();
    }

    /**
     * <p>updateMenu.</p>
     *
     * @param resources a {@link RoleDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(value = "修改角色菜单", category = LogCategoryType.MANAGER, type = LogType.UPDATE, module = "角色")
    @ApiOperation("修改角色菜单")
    @PutMapping(value = "/menu")
    @PreAuthorize("@el.check('roles:edit')")
    public R updateMenu(@RequestBody RoleDO resources) {
        RoleDTO role = roleService.findById(resources.getId());
        getLevels(role.getLevel());
        roleService.updateMenu(resources);
        return R.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link R} object.
     */
    @LogRecord(value = "删除", category = LogCategoryType.MANAGER, type = LogType.DELETE, module = "角色")
    @ApiOperation("删除角色")
    @DeleteMapping
    @PreAuthorize("@el.check('roles:del')")
    public R delete(@RequestBody Set<Long> ids) {
        for (Long id : ids) {
            RoleDTO role = roleService.findById(id);
            getLevels(role.getLevel());
        }
        // 验证是否被用户关联
        roleService.verification(ids);
        roleService.delete(ids);
        return R.ok();
    }

    /**
     * 获取用户的角色级别
     *
     * @return /
     */
    private int getLevels(Integer level) {
        List<Integer> levels = roleService.findByUsersId(SecurityUtil.getUserId()).stream().map(RoleSmallDTO::getLevel).collect(Collectors.toList());
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
     * @return a {@link R} object.
     */
    @ApiOperation("查询下属用户")
    @GetMapping("/users")
    @PreAuthorize("@el.check('roles:edit')")
    @PermissionData(deptIdInFieldName = DataScopeVO.F_SQL_SCOPE_NAME)
    public R<MyPage<UserVO>> listByRole(PageParam pageable, @RequestParam Long roleId, @RequestParam(required = false) String blurry) {
        return R.ok(userRoleService.listUserById(pageable, roleId, blurry));
    }

    /**
     * <p>query.</p>
     *
     * @param roleId   角色id.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询非下属用户")
    @GetMapping("/not/users")
    @PreAuthorize("@el.check('roles:edit')")
    @PermissionData(deptIdInFieldName = DataScopeVO.F_SQL_SCOPE_NAME)
    public R<MyPage<UserVO>> listNotByRole(PageParam pageable, @RequestParam Long roleId, @RequestParam(required = false) String blurry) {
        return R.ok(userRoleService.listNotUserById(pageable, roleId, blurry));
    }

    @LogRecord(value = "删除下属用户", category = LogCategoryType.MANAGER, type = LogType.DELETE, module = "角色")
    @ApiOperation("删除下属用户")
    @DeleteMapping("/users")
    @PreAuthorize("@el.check('roles:del')")
    public R deleteUsers(@Valid @RequestBody List<UserRoleDTO> list) {
        userRoleService.deleteUsers(list);
        return R.ok();
    }

    @LogRecord(value = "添加下属用户", category = LogCategoryType.MANAGER, type = LogType.ADD, module = "角色")
    @ApiOperation("添加下属用户")
    @PostMapping("/users")
    @PreAuthorize("@el.check('roles:del')")
    public R addUsers(@Valid @RequestBody List<UserRoleDTO> list) {
        userRoleService.addUsers(list);
        return R.ok();
    }

    @GetMapping("/list-all-simple")
    @Operation(summary = "获取角色精简信息列表", description = "只包含被开启的角色，主要用于前端的下拉选项")
    public R<List<RoleSmallDTO>> getSimpleRoleList() {
        return R.ok(roleService.getSimpleRoleList());
    }
}
