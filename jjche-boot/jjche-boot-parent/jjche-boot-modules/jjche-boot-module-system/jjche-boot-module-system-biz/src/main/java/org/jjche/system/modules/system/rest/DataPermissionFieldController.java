package org.jjche.system.modules.system.rest;

import cn.hutool.core.lang.Assert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.dto.PermissionDataResourceDTO;
import org.jjche.common.dto.PermissionDataRuleDTO;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.vo.DataPermissionFieldResultVO;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.system.modules.permission.api.DataPermissionFieldApi;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldDTO;
import org.jjche.system.modules.system.api.dto.DataPermissionFieldQueryCriteriaDTO;
import org.jjche.system.modules.system.api.vo.DataPermissionFieldVO;
import org.jjche.system.modules.system.service.DataPermissionFieldService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * <p>MenuController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-03
 */

@RequiredArgsConstructor
@Api(tags = "系统：数据字段管理")
@SysRestController("data_permission_fields")
public class DataPermissionFieldController extends BaseController implements DataPermissionFieldApi {

    private final DataPermissionFieldService dataPermissionFieldService;

    /**
     * <p>create.</p>
     *
     * @param resources a {@link DataPermissionFieldDTO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "数据字段"
    )
    @ApiOperation("新增数据字段")
    @PostMapping
    @PreAuthorize("@el.check('menu:add')")
    public R create(@Validated @RequestBody DataPermissionFieldDTO resources) {
        Assert.isNull(resources.getId(), "A new DataPermissionFieldDO cannot already have an ID");
        dataPermissionFieldService.create(resources);
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
            type = LogType.DELETE, module = "数据字段"
    )
    @ApiOperation("删除数据字段")
    @DeleteMapping()
    @PreAuthorize("@el.check('menu:del')")
    public R delete(@RequestBody Set<Long> ids) {
        dataPermissionFieldService.delete(ids);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link DataPermissionFieldDTO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "数据字段"
    )
    @ApiOperation("修改数据字段")
    @PutMapping
    @PreAuthorize("@el.check('menu:edit')")
    public R update(@Validated(DataPermissionFieldDTO.Update.class) @RequestBody DataPermissionFieldDTO resources) {
        dataPermissionFieldService.update(resources);
        return R.ok();
    }

    /**
     * <p>getById.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "数据字段-查询单个")
    @PreAuthorize("@el.check('menu:list')")
    public R<DataPermissionFieldVO> getById(@PathVariable Long id) {
        return R.ok(this.dataPermissionFieldService.getVoById(id));
    }


    /**
     * <p>query.</p>
     *
     * @param criteria a {@link DataPermissionFieldQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询数据字段")
    @GetMapping
    @PreAuthorize("@el.check('menu:list')")
    public R<MyPage<DataPermissionFieldVO>> query(PageParam pageable, DataPermissionFieldQueryCriteriaDTO criteria) {
        return R.ok(dataPermissionFieldService.pageQuery(pageable, criteria));
    }

    @Override
    @PostMapping("list-permission-data-resource")
    public List<DataPermissionFieldResultVO> listPermissionDataResource(@RequestBody PermissionDataResourceDTO dto) {
        return dataPermissionFieldService.listPermissionDataResource(dto);
    }

    @Override
    @GetMapping("list-permission-data-ruleByUserId")
    public List<PermissionDataRuleDTO> listPermissionDataRuleByUserId(@RequestParam Long userId){
        return dataPermissionFieldService.listPermissionDataRuleByUserId(userId);
    }
}
