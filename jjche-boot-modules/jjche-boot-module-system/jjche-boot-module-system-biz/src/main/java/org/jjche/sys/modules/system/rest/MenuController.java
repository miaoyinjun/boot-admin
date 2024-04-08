package org.jjche.sys.modules.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.exception.util.AssertUtil;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.core.util.SecurityUtil;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.jjche.sys.modules.system.domain.MenuDO;
import org.jjche.sys.modules.system.dto.MenuDTO;
import org.jjche.sys.modules.system.dto.MenuQueryCriteriaDTO;
import org.jjche.sys.modules.system.mapstruct.MenuMapStruct;
import org.jjche.sys.modules.system.service.MenuService;
import org.jjche.sys.modules.system.vo.MenuVO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>MenuController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-03
 */

@Api(tags = "系统：菜单管理")
@RestController
@RequestMapping("menus")
@RequiredArgsConstructor
public class MenuController extends BaseController {

    private static final String ENTITY_NAME = "menu";
    private final MenuService menuService;
    private final MenuMapStruct menuMapStruct;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link MenuQueryCriteriaDTO} object.
     * @throws java.lang.Exception if any.
     */
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('menu:list')")
    public void download(HttpServletResponse response, MenuQueryCriteriaDTO criteria) throws Exception {
        menuService.download(menuService.queryAll(criteria, false), response);
    }

    /**
     * <p>buildMenus.</p>
     *
     * @return a {@link R} object.
     */
    @GetMapping(value = "/build")
    @ApiOperation("获取前端所需菜单")
    public R<List<MenuVO>> buildMenus() {
        List<MenuDTO> menuDtoList = menuService.findByUser(SecurityUtil.getUserId());
        List<MenuDTO> menuDtos = menuService.buildTree(menuDtoList);
        return R.ok(menuService.buildMenus(menuDtos));
    }

    /**
     * <p>
     * 返回全部的菜单
     * </p>
     *
     * @param pid    父id
     * @param roleId 角色id
     * @return 菜单
     * @author miaoyj
     * @since 2020-12-09
     */
    @ApiOperation("返回全部的菜单")
    @GetMapping(value = "/lazy")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public R<List<MenuDTO>> query(@RequestParam Long pid,
                                  @RequestParam Long roleId) {
        return R.ok(menuService.getMenus(pid, roleId));
    }

    /**
     * <p>child.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @ApiOperation("根据菜单ID返回所有子节点ID，包含自身ID")
    @GetMapping(value = "/child")
    @PreAuthorize("@el.check('menu:list','roles:list')")
    public R<Set<Long>> child(@RequestParam Long id) {
        Set<MenuDO> menuSet = new HashSet<>();
        List<MenuDTO> menuList = menuService.getMenus(id, null);
        menuSet.add(menuService.findOne(id));
        menuSet = menuService.getChildMenus(menuMapStruct.toDO(menuList), menuSet);
        Set<Long> ids = menuSet.stream().map(MenuDO::getId).collect(Collectors.toSet());
        return R.ok(ids);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link MenuQueryCriteriaDTO} object.
     * @return a {@link R} object.
     * @throws java.lang.Exception if any.
     */
    @ApiOperation("查询菜单")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public R<MyPage<MenuDTO>> query(MenuQueryCriteriaDTO criteria) throws Exception {
        List<MenuDTO> menuDtoList = menuService.queryAll(criteria, true);
        MyPage<MenuDTO> myPage = new MyPage<>();
        myPage.setRecords(menuDtoList);
        myPage.setTotal(menuDtoList.size());
        return R.ok(myPage);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link MenuQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询菜单分页")
    @GetMapping("/page")
    @PreAuthorize("@el.check('menu:list')")
    public R<MyPage<MenuDTO>> query(MenuQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(menuService.queryPage(criteria, pageable));
    }

    /**
     * <p>getSuperior.</p>
     *
     * @param ids a {@link java.util.List} object.
     * @return a {@link R} object.
     */
    @ApiOperation("查询菜单:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('menu:list')")
    public R<List<MenuDTO>> getSuperior(@RequestBody List<Long> ids) {
        Set<MenuDTO> menuDtos = new LinkedHashSet<>();
        if (CollectionUtil.isNotEmpty(ids)) {
            for (Long id : ids) {
                MenuDTO menuDto = menuService.findById(id);
                menuDtos.addAll(menuService.getSuperior(menuDto, new ArrayList<>()));
            }
            return R.ok(menuService.buildTree(new ArrayList<>(menuDtos)));
        }
        return R.ok(menuService.getMenus(null, null));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link MenuDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "菜单"
    )
    @ApiOperation("新增菜单")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public R create(@Validated @RequestBody MenuDO resources) {
        AssertUtil.isFalse(resources.getId() != null, SysErrorCodeEnum.ID_ALREADY_ERROR);
        menuService.create(resources);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link MenuDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "菜单"
    )
    @ApiOperation("修改菜单")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public R update(@Validated @RequestBody MenuDO resources) {
        menuService.update(resources);
        return R.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "菜单"
    )
    @ApiOperation("删除菜单")
    @DeleteMapping
    @PreAuthorize("@el.check('menu:del')")
    public R delete(@RequestBody Set<Long> ids) {
        Set<MenuDO> menuSet = new HashSet<>();
        for (Long id : ids) {
            List<MenuDTO> menuList = menuService.getMenus(id, null);
            menuSet.add(menuService.findOne(id));
            menuSet = menuService.getChildMenus(menuMapStruct.toDO(menuList), menuSet);
        }
        menuService.delete(menuSet);
        return R.ok();
    }
}
