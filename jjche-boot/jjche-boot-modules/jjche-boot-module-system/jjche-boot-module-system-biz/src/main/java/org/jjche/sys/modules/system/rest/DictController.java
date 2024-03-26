package org.jjche.sys.modules.system.rest;

import cn.hutool.core.lang.Assert;
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
import org.jjche.sys.modules.system.dto.DictDTO;
import org.jjche.sys.modules.system.dto.DictQueryCriteriaDTO;
import org.jjche.sys.modules.system.domain.DictDO;
import org.jjche.sys.modules.system.service.DictService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;

/**
 * <p>DictController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-04-10
 */
@Api(tags = "系统：字典管理")
@RestController
@RequestMapping("dict")
@RequiredArgsConstructor
public class DictController extends BaseController {

    private static final String ENTITY_NAME = "dict";
    private final DictService dictService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link DictQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出字典数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('dict:list')")
    public void download(HttpServletResponse response, DictQueryCriteriaDTO criteria) throws IOException {
        dictService.download(dictService.queryAll(criteria), response);
    }

    /**
     * <p>queryAll.</p>
     *
     * @return a {@link R} object.
     */
    @GetMapping(value = "/all")
    @PreAuthorize("@el.check('dict:list')")
    public R<List<DictDTO>> queryAll() {
        return R.ok(dictService.queryAll(new DictQueryCriteriaDTO()));
    }

    /**
     * <p>query.</p>
     *
     * @param resources a {@link DictQueryCriteriaDTO} object.
     * @param pageable  /
     * @return a {@link R} object.
     */
    @ApiOperation("查询字典")
    @GetMapping
    @PreAuthorize("@el.check('dict:list')")
    public R<MyPage> query(DictQueryCriteriaDTO resources, PageParam pageable) {
        return R.ok(dictService.queryAll(resources, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link DictDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "字典"
    )
    @ApiOperation("新增字典")
    @PostMapping
    @PreAuthorize("@el.check('dict:add')")
    public R create(@Validated @RequestBody DictDO resources) {
        Assert.isNull(resources.getId(), "A new " + ENTITY_NAME + " cannot already have an ID");
        dictService.create(resources);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link DictDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "字典"
    )
    @ApiOperation("修改字典")
    @PutMapping
    @PreAuthorize("@el.check('dict:edit')")
    public R update(@Validated @RequestBody DictDO resources) {
        dictService.update(resources);
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
            type = LogType.DELETE, module = "字典"
    )
    @ApiOperation("删除字典")
    @DeleteMapping
    @PreAuthorize("@el.check('dict:del')")
    public R delete(@RequestBody Set<Long> ids) {
        dictService.delete(ids);
        return R.ok();
    }
}
