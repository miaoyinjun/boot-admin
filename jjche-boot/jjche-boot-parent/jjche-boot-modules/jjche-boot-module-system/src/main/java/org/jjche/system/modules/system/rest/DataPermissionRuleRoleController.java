package org.jjche.system.modules.system.rest;


import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.system.modules.system.api.dto.DataPermissionRuleRoleDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionRuleRoleQueryCriteriaDTO;
import org.jjche.system.modules.system.api.vo.DataPermissionRuleVO;
import org.jjche.system.modules.system.service.DataPermissionRuleRoleService;
import org.jjche.system.modules.system.service.DataPermissionRuleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>
 * 数据规则权限 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-11-01
 */
@Api(tags = "系统：数据规则权限")
@ApiSupport(order = 1, author = "miaoyj")
@SysRestController("data_permission_rule_roles")
@RequiredArgsConstructor
public class DataPermissionRuleRoleController extends BaseController {

    private final DataPermissionRuleRoleService sysDataPermissionRuleRoleService;
    private final DataPermissionRuleService sysDataPermissionRuleService;

    /**
     * <p>create.</p>
     *
     * @param dto a {@link DataPermissionRuleRoleDTO} object.
     * @return a {@link R} object.
     */
    @PostMapping
    @ApiOperation(value = "数据规则权限-保存")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('roles:edit')")
    @LogRecord(
            value = "保存",
            category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据规则权限"
    )
    public R create(@Validated @RequestBody DataPermissionRuleRoleDTO dto) {
        sysDataPermissionRuleRoleService.save(dto);
        return R.ok();
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param page  a {@link PageParam} object.
     * @param query a {@link DataPermissionRuleRoleQueryCriteriaDTO} object.
     * @return a {@link R} object.
     */
    @GetMapping
    @ApiOperation(value = "数据规则权限-列表")
    @PreAuthorize("@el.check('roles:list')")
    public R<MyPage<DataPermissionRuleVO>> pageQuery(PageParam page,
                                                     @Validated DataPermissionRuleRoleQueryCriteriaDTO query) {
        return R.ok(sysDataPermissionRuleService.pageByMenuIdAndRoleId(page, query));
    }
}
