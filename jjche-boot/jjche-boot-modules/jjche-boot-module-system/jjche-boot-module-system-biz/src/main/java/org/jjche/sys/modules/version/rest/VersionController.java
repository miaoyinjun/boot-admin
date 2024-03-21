package org.jjche.sys.modules.version.rest;

import com.alicp.jetcache.anno.CacheInvalidate;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
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
import org.jjche.security.annotation.rest.IgnoreGetMapping;
import org.jjche.sys.modules.version.api.dto.VersionDTO;
import org.jjche.sys.modules.version.api.dto.VersionQueryCriteriaDTO;
import org.jjche.sys.modules.version.api.vo.VersionVO;
import org.jjche.sys.modules.version.service.VersionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 版本 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-04-23
 */
@Api(tags = "系统：版本")
@RestController
@RequestMapping("versions")
@RequiredArgsConstructor
public class VersionController extends BaseController {

    private final VersionService versionService;

    /**
     * <p>create.</p>
     *
     * @param dto a {@link VersionDTO} object.
     * @return a {@link R} object.
     */
    @ApiOperation("版本-新增")
    @PostMapping
    @LogRecord(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "版本"
    )
    @PreAuthorize("@el.check('version:add')")
    public R create(@Validated @RequestBody VersionDTO dto) {
        versionService.save(dto);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param dto a {@link VersionDTO} object.
     * @return a {@link R} object.
     */
    @ApiOperation("版本-修改")
    @PutMapping
    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "版本"
    )
    @PreAuthorize("@el.check('version:edit')")
    public R update(@Validated(VersionDTO.VersionDtoUpdateValid.class)
                    @RequestBody VersionDTO dto) {
        versionService.update(dto);
        return R.ok();
    }

    /**
     * <p>getById.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @ApiOperation("版本-获取单个")
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('version:list')")
    public R<VersionVO> getById(@PathVariable Long id) {
        return R.ok(this.versionService.getVoById(id));
    }

    /**
     * <p>pageQuery.</p>
     *
     * @param query a {@link VersionQueryCriteriaDTO} object.
     * @param page  page
     * @return a {@link R} object.
     */
    @ApiOperation("版本-分页")
    @GetMapping
    @PreAuthorize("@el.check('version:list')")
    public R<MyPage<VersionVO>> pageQuery(VersionQueryCriteriaDTO query, PageParam page) {
        return R.ok(versionService.queryAll(query, page));
    }

    /**
     * <p>activated.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @ApiOperation("版本-激活")
    @PutMapping("/{id}")
    @CacheInvalidate(name = "versions:", key = "'latest'")
    @LogRecord(
            value = "激活", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "版本"
    )
    @PreAuthorize("@el.check('version:edit')")
    public R activated(@PathVariable Long id) {
        versionService.updateActivated(id);
        return R.ok();
    }

    /**
     * <p>versionLatest.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("版本-获取最新")
    @IgnoreGetMapping("/latest")
    @Cached(name = "versions:", key = "'latest'", cacheType = CacheType.REMOTE)
    public R<String> versionLatest() {
        return R.ok(versionService.versionLatest());
    }
}
