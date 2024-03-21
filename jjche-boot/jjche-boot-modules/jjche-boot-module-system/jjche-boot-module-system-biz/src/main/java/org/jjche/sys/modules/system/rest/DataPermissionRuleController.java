package org.jjche.sys.modules.system.rest;

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
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.sys.modules.system.api.dto.DataPermissionRuleDTO;
import org.jjche.sys.modules.system.api.dto.DataPermissionRuleQueryCriteriaDTO;
import org.jjche.sys.modules.system.api.vo.DataPermissionRuleVO;
import org.jjche.sys.modules.system.service.DataPermissionRuleService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

/**
 * <p>
 * 数据规则 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.1-SNAPSHOT
 * @since 2021-10-27
 */
@Api(tags = "系统：数据规则")
@ApiSupport(order = 1, author = "miaoyj")
@RestController
@RequestMapping("data_permission_rules")
@RequiredArgsConstructor
public class DataPermissionRuleController extends BaseController {

    private final DataPermissionRuleService sysDataPermissionRuleService;

    /**
     * <p>create.</p>
     *
     * @param dto a {@link DataPermissionRuleDTO} object.
     * @return a {@link R} object.
     */
    @PostMapping
    @ApiOperation(value = "数据规则-新增")
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('menu:add')")
    @LogRecord(
            value = "新增",
            category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据规则"
    )
    public R create(@Validated @RequestBody DataPermissionRuleDTO dto) {
        sysDataPermissionRuleService.save(dto);
        return R.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link R} object.
     */
    @DeleteMapping
    @ApiOperation(value = "数据规则-删除")
    @PreAuthorize("@el.check('menu:del')")
    @LogRecord(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "数据规则",
            prefix = "sysDataPermissionRule", bizNo = "{{#ids}}"
    )
    public R delete(@RequestBody Set<Long> ids) {
        sysDataPermissionRuleService.delete(ids);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param dto a {@link DataPermissionRuleDTO} object.
     * @return a {@link R} object.
     */
    @PutMapping
    @ApiOperation(value = "数据规则-修改")
    @PreAuthorize("@el.check('menu:edit')")
    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "数据规则"
    )
    public R update(@Validated(DataPermissionRuleDTO.Update.class)
                    @RequestBody DataPermissionRuleDTO dto) {
        sysDataPermissionRuleService.update(dto);
        return R.ok();
    }

    /**
     * <p>getById.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "数据规则-查询单个")
    @PreAuthorize("@el.check('menu:list')")
    public R<DataPermissionRuleVO> getById(@PathVariable Long id) {
        return R.ok(this.sysDataPermissionRuleService.getVoById(id));
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param page  a {@link PageParam} object.
     * @param query a {@link DataPermissionRuleQueryCriteriaDTO} object.
     * @return a {@link R} object.
     */
    @GetMapping
    @ApiOperation(value = "数据规则-列表")
    @PreAuthorize("@el.check('menu:list')")
    public R<MyPage<DataPermissionRuleVO>> pageQuery(PageParam page,
                                                     @Validated DataPermissionRuleQueryCriteriaDTO query) {
        return R.ok(sysDataPermissionRuleService.pageQuery(page, query));
    }

}
