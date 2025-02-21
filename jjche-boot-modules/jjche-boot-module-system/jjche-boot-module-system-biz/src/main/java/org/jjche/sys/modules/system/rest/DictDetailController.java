package org.jjche.sys.modules.system.rest;

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
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.sys.enums.SysErrorCodeEnum;
import org.jjche.sys.modules.system.domain.DictDetailDO;
import org.jjche.sys.modules.system.dto.DictDetailDTO;
import org.jjche.sys.modules.system.dto.DictDetailQueryCriteriaDTO;
import org.jjche.sys.modules.system.service.DictDetailService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>DictDetailController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Api(tags = "系统：字典详情管理")
@RestController
@RequestMapping("dictDetail")
@RequiredArgsConstructor
public class DictDetailController extends BaseController {

    private static final String ENTITY_NAME = "dictDetail";
    private final DictDetailService dictDetailService;

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link DictDetailQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询字典详情")
    @GetMapping
    public R<MyPage<DictDetailDTO>> query(DictDetailQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(dictDetailService.queryAll(criteria, pageable));
    }

    /**
     * <p>getDictDetailMaps.</p>
     *
     * @param dictName a {@link java.lang.String} object.
     * @return a {@link R} object.
     */
    @ApiOperation("查询多个字典详情")
    @GetMapping(value = "/map")
    public R<Map<String, List<DictDetailDTO>>> getDictDetailMaps(@RequestParam String dictName) {
        String[] names = dictName.split("[,，]");
        Map<String, List<DictDetailDTO>> dictMap = new HashMap<>(16);
        for (String name : names) {
            dictMap.put(name, dictDetailService.getDictByName(name));
        }
        return R.ok(dictMap);
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link DictDetailDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "新增字典详情", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "字典"
    )
    @ApiOperation("新增字典详情")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public R create(@Validated @RequestBody DictDetailDO resources) {
        AssertUtil.isFalse(resources.getId() != null, SysErrorCodeEnum.ID_ALREADY_ERROR);
        dictDetailService.create(resources);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link DictDetailDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "字典"
    )
    @ApiOperation("修改字典详情")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public R update(@Validated @RequestBody DictDetailDO resources) {
        dictDetailService.update(resources);
        return R.ok();
    }

    /**
     * <p>delete.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "删除字典详情", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "字典"
    )
    @ApiOperation("删除字典详情")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("@el.check('dict:del')")
    public R delete(@PathVariable Long id) {
        dictDetailService.delete(id);
        return R.ok();
    }
}
