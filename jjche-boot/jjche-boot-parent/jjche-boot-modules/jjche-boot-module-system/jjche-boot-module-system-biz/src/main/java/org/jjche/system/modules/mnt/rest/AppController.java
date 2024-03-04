package org.jjche.system.modules.mnt.rest;

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
import org.jjche.system.modules.mnt.domain.AppDO;
import org.jjche.system.modules.mnt.dto.AppDTO;
import org.jjche.system.modules.mnt.dto.AppQueryCriteriaDTO;
import org.jjche.system.modules.mnt.service.AppService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>AppController class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@RequiredArgsConstructor
@Api(tags = "运维：应用管理")
@SysRestController("app")
public class AppController extends BaseController {

    private final AppService appService;

    @ApiOperation("导出应用数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('app:list')")
    public void download(HttpServletResponse response, AppQueryCriteriaDTO criteria) throws IOException {
        appService.download(appService.queryAll(criteria), response);
    }

    @ApiOperation(value = "查询应用")
    @GetMapping
    @PreAuthorize("@el.check('app:list')")
    public R<MyPage<AppDTO>> query(AppQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(appService.queryAll(criteria, pageable));
    }

    @LogRecord(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "应用"
    )
    @ApiOperation(value = "新增应用")
    @PostMapping
    @PreAuthorize("@el.check('app:add')")
    public R create(@Validated @RequestBody AppDO resources) {
        appService.create(resources);
        return R.ok();
    }

    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "应用"
    )
    @ApiOperation(value = "修改应用")
    @PutMapping
    @PreAuthorize("@el.check('app:edit')")
    public R update(@Validated @RequestBody AppDO resources) {
        appService.update(resources);
        return R.ok();
    }

    @LogRecord(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "应用"
    )
    @ApiOperation(value = "删除应用")
    @DeleteMapping
    @PreAuthorize("@el.check('app:del')")
    public R delete(@RequestBody Set<Long> ids) {
        appService.delete(ids);
        return R.ok();
    }
}
