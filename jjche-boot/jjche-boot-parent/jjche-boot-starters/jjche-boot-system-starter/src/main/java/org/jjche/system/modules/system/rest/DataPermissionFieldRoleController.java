package org.jjche.system.modules.system.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldRoleDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldRoleQueryCriteriaDTO;
import org.jjche.system.modules.system.api.vo.DataPermissionFieldRoleVO;
import org.jjche.system.modules.system.service.DataPermissionFieldRoleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 数据字段角色 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-04
 */
@Api(tags = "系统：数据字段角色")
@ApiSupport(order = 1, author = "miaoyj")
@SysRestController("data_permission_field_roles")
@RequiredArgsConstructor
public class DataPermissionFieldRoleController extends BaseController {

    private final DataPermissionFieldRoleService sysDataPermissionFieldRoleService;

    /**
     * <p>create.</p>
     *
     * @param dto a {@link DataPermissionFieldRoleDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @PostMapping
    @ApiOperation(value = "数据字段角色-保存")
    @PreAuthorize("@el.check('roles:edit')")
    @LogRecord(
            value = "保存",
            category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据字段角色"
    )
    public ResultWrapper create(@Validated @RequestBody DataPermissionFieldRoleDTO dto) {
        sysDataPermissionFieldRoleService.save(dto);
        return ResultWrapper.ok();
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param page  a {@link PageParam} object.
     * @param query a {@link DataPermissionFieldRoleQueryCriteriaDTO} object.
     * @return a {@link ResultWrapper} object.
     */
    @GetMapping
    @ApiOperation(value = "数据字段角色-列表")
    @PreAuthorize("@el.check('roles:list')")
    public ResultWrapper<MyPage<DataPermissionFieldRoleVO>> pageQuery(PageParam page,
                                                                      @Validated DataPermissionFieldRoleQueryCriteriaDTO query) {
        return ResultWrapper.ok(sysDataPermissionFieldRoleService.page(page, query));
    }

}
