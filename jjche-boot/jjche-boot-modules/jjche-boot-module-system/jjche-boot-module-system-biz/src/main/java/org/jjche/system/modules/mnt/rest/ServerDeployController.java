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
import org.jjche.system.modules.mnt.domain.ServerDeployDO;
import org.jjche.system.modules.mnt.api.dto.ServerDeployDTO;
import org.jjche.system.modules.mnt.api.dto.ServerDeployQueryCriteriaDTO;
import org.jjche.system.modules.mnt.service.ServerDeployService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>ServerDeployController class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@Api(tags = "运维：服务器管理")
@RequiredArgsConstructor
@SysRestController("serverDeploy")
public class ServerDeployController extends BaseController {

    private final ServerDeployService serverDeployService;

    @ApiOperation("导出服务器数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('serverDeploy:list')")
    public void download(HttpServletResponse response, ServerDeployQueryCriteriaDTO criteria) throws IOException {
        serverDeployService.download(serverDeployService.queryAll(criteria), response);
    }

    @ApiOperation(value = "查询服务器")
    @GetMapping
    @PreAuthorize("@el.check('serverDeploy:list')")
    public R<MyPage<ServerDeployDTO>> query(
            ServerDeployQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(serverDeployService.queryAll(criteria, pageable));
    }

    @LogRecord(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "服务器"
    )
    @ApiOperation(value = "新增服务器")
    @PostMapping
    @PreAuthorize("@el.check('serverDeploy:add')")
    public R create(@Validated @RequestBody ServerDeployDO resources) {
        serverDeployService.create(resources);
        return R.ok();
    }

    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "服务器"
    )
    @ApiOperation(value = "修改服务器")
    @PutMapping
    @PreAuthorize("@el.check('serverDeploy:edit')")
    public R update(@Validated @RequestBody ServerDeployDO resources) {
        serverDeployService.update(resources);
        return R.ok();
    }

    @LogRecord(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "服务器"
    )
    @ApiOperation(value = "删除Server")
    @DeleteMapping
    @PreAuthorize("@el.check('serverDeploy:del')")
    public R delete(@RequestBody Set<Long> ids) {
        serverDeployService.delete(ids);
        return R.ok();
    }

    @ApiOperation(value = "测试连接服务器")
    @PostMapping("/testConnect")
    @PreAuthorize("@el.check('serverDeploy:add')")
    public R<Boolean> testConnect(@Validated @RequestBody ServerDeployDO resources) {
        return R.ok(serverDeployService.testConnect(resources));
    }
}
