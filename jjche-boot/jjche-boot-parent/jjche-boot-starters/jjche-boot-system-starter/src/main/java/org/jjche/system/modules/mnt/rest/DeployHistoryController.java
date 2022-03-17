package org.jjche.system.modules.mnt.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.response.response.ResultWrapper;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecordAnnotation;
import org.jjche.mybatis.param.MyPage;
import org.jjche.mybatis.param.PageParam;
import org.jjche.system.modules.mnt.dto.DeployHistoryDTO;
import org.jjche.system.modules.mnt.dto.DeployHistoryQueryCriteriaDTO;
import org.jjche.system.modules.mnt.service.DeployHistoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>DeployHistoryController class.</p>
 *
 * @author zhanghouying
 * @version 1.0.8-SNAPSHOT
 * @since 2019-08-24
 */
@RequiredArgsConstructor
@Api(tags = "运维：部署历史管理")
@SysRestController("deployHistory")
public class DeployHistoryController extends BaseController {

    private final DeployHistoryService deployhistoryService;

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link DeployHistoryQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出部署历史数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('deployHistory:list')")
    public void download(HttpServletResponse response, DeployHistoryQueryCriteriaDTO criteria) throws IOException {
        deployhistoryService.download(deployhistoryService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link DeployHistoryQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link ResultWrapper} object.
     */
    @ApiOperation(value = "查询部署历史")
    @GetMapping
    @PreAuthorize("@el.check('deployHistory:list')")
    public ResultWrapper<MyPage<DeployHistoryDTO>> query(DeployHistoryQueryCriteriaDTO criteria, PageParam pageable) {
        return ResultWrapper.ok(deployhistoryService.queryAll(criteria, pageable));
    }

    /**
     * <p>delete.</p>
     *
     * @param ids a {@link java.util.Set} object.
     * @return a {@link ResultWrapper} object.
     */
    @LogRecordAnnotation(
            value = "删除", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "部署历史"
    )
    @ApiOperation(value = "删除部署历史")
    @DeleteMapping
    @PreAuthorize("@el.check('deployHistory:del')")
    public ResultWrapper delete(@RequestBody Set<String> ids) {
        deployhistoryService.delete(ids);
        return ResultWrapper.ok();
    }
}
