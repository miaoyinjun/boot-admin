package org.jjche.system.modules.system.rest;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.annotation.PermissionData;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.core.wrapper.response.ResultWrapper;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.mybatis.param.MyPage;
import org.jjche.system.modules.system.api.dto.DeptDTO;
import org.jjche.system.modules.system.api.dto.DeptQueryCriteriaDTO;
import org.jjche.system.modules.system.domain.DeptDO;
import org.jjche.system.modules.system.service.DeptService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * <p>DeptController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-03-25
 */
@RequiredArgsConstructor
@Api(tags = "系统：部门管理")
@SysRestController("dept")
public class DeptController extends BaseController {

    private static final String ENTITY_NAME = "dept";
    private final DeptService deptService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link DeptQueryCriteriaDTO} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "导出", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "部门"
    )
    @ApiOperation("导出部门数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dept:list')")
    @PermissionData(deptIdInFieldName = "id")
    public void download(HttpServletResponse response, DeptQueryCriteriaDTO criteria) throws Exception {
        deptService.download(deptService.queryAll(criteria, false), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link DeptQueryCriteriaDTO} object.
     * @return a {@link ResultWrapper} object.
     * @throws java.lang.Exception if any.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "部门"
    )
    @ApiOperation("查询部门")
    @GetMapping
    @PreAuthorize("@el.check('user:list','dept:list')")
    @PermissionData(deptIdInFieldName = "id")
    public ResultWrapper<MyPage> query(DeptQueryCriteriaDTO criteria) throws Exception {
        List<DeptDTO> deptDtos = deptService.queryAll(criteria, true);
        MyPage<DeptDTO> myPage = new MyPage<>();
        myPage.setRecords(deptDtos);
        myPage.setTotal(deptDtos.size());
        return ResultWrapper.ok(myPage);
    }

    /**
     * <p>getSuperior.</p>
     *
     * @param ids a {@link java.util.List} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "查询", category = LogCategoryType.MANAGER,
            type = LogType.SELECT, module = "部门"
    )
    @ApiOperation("查询部门:根据ID获取同级与上级数据")
    @PostMapping("/superior")
    @PreAuthorize("@el.check('user:list','dept:list')")
    @PermissionData(deptIdInFieldName = "id")
    public ResultWrapper<MyPage<List<DeptDTO>>> getSuperior(@RequestBody List<Long> ids) {
        Set<DeptDTO> deptDtos = new LinkedHashSet<>();
        for (Long id : ids) {
            DeptDTO deptDto = deptService.findById(id);
            List<DeptDTO> depts = deptService.getSuperior(deptDto, new ArrayList<>());
            deptDtos.addAll(depts);
        }
        return ResultWrapper.ok(deptService.buildTree(new ArrayList<>(deptDtos)));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link DeptDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "部门"
    )
    @ApiOperation("新增部门")
    @PostMapping
    @PreAuthorize("@el.check('dept:add')")
    public ResultWrapper create(@Validated @RequestBody DeptDO resources) {
        Assert.isFalse(resources.getId() != null, "A new " + ENTITY_NAME + " cannot already have an ID");
        deptService.create(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link DeptDO} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "部门"
    )
    @ApiOperation("修改部门")
    @PutMapping
    @PreAuthorize("@el.check('dept:edit')")
    public ResultWrapper update(@Validated @RequestBody DeptDO resources) {
        deptService.update(resources);
        return ResultWrapper.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "部门"
    )
    @ApiOperation("删除部门")
    @DeleteMapping
    @PreAuthorize("@el.check('dept:del')")
    public ResultWrapper delete(@RequestBody Set<Long> ids) {
        Set<DeptDTO> deptDtos = new HashSet<>();
        for (Long id : ids) {
            List<DeptDO> deptList = deptService.findByPid(id);
            deptDtos.add(deptService.findById(id));
            if (CollectionUtil.isNotEmpty(deptList)) {
                deptDtos = deptService.getDeleteDepts(deptList, deptDtos);
            }
        }
        // 验证是否被角色或用户关联
        deptService.verification(deptDtos);
        deptService.delete(deptDtos);
        return ResultWrapper.ok();
    }
}
