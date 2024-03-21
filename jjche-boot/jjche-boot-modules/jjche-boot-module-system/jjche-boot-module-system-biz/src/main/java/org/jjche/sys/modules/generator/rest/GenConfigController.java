package org.jjche.sys.modules.generator.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.sys.modules.generator.domain.GenConfigDO;
import org.jjche.sys.modules.generator.service.GenConfigService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>GenConfigController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-14
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("genConfig")
@Api(tags = "系统：代码生成器配置管理")
public class GenConfigController extends BaseController {

    private final GenConfigService genConfigService;

    @ApiOperation("查询")
    @GetMapping(value = "/{tableName}")
    @PreAuthorize("@el.check('generator:list')")
    public R<GenConfigDO> query(@PathVariable String tableName) {
        return R.ok(genConfigService.find(tableName));
    }

    /**
     * <p>update.</p>
     *
     * @param genConfig a {@link GenConfigDO} object.
     * @return a {@link R} object.
     */
    @ApiOperation("修改")
    @PutMapping
    @PreAuthorize("@el.check('generator:list')")
    public R<GenConfigDO> update(@Validated @RequestBody GenConfigDO genConfig) {
        return R.ok(genConfigService.update(genConfig));
    }
}
