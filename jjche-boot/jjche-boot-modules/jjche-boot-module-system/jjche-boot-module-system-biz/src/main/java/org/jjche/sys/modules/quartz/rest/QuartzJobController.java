package org.jjche.sys.modules.quartz.rest;

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
import org.jjche.sys.modules.quartz.dto.JobQueryCriteriaDTO;
import org.jjche.sys.modules.quartz.domain.QuartzJobDO;
import org.jjche.sys.modules.quartz.domain.QuartzLogDO;
import org.jjche.sys.modules.quartz.service.QuartzJobService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * <p>QuartzJobController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2019-01-07
 */
@RequiredArgsConstructor
@Api(tags = "系统：定时任务管理")
@RestController
@RequestMapping("jobs")
public class QuartzJobController extends BaseController {

    private static final String ENTITY_NAME = "quartzJob";
    private final QuartzJobService quartzJobService;

    /**
     * <p>query.</p>
     *
     * @param criteria a query object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询定时任务")
    @GetMapping
    @PreAuthorize("@el.check('timing:list')")
    public R<MyPage<QuartzJobDO>> query(JobQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(quartzJobService.queryAll(criteria, pageable));
    }

    /**
     * <p>download.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link JobQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出任务数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('timing:list')")
    public void download(HttpServletResponse response, JobQueryCriteriaDTO criteria) throws IOException {
        quartzJobService.download(quartzJobService.queryAll(criteria), response);
    }

    /**
     * <p>downloadLog.</p>
     *
     * @param response a {@link javax.servlet.http.HttpServletResponse} object.
     * @param criteria a {@link JobQueryCriteriaDTO} object.
     * @throws java.io.IOException if any.
     */
    @ApiOperation("导出日志数据")
    @GetMapping(value = "/logs/download")
    @PreAuthorize("@el.check('timing:list')")
    public void downloadLog(HttpServletResponse response, JobQueryCriteriaDTO criteria) throws IOException {
        quartzJobService.downloadLog(quartzJobService.queryAllLog(criteria), response);
    }

    /**
     * <p>queryJobLog.</p>
     *
     * @param criteria a {@link JobQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @ApiOperation("查询任务执行日志")
    @GetMapping(value = "/logs")
    @PreAuthorize("@el.check('timing:list')")
    public R<MyPage<QuartzLogDO>> queryJobLog(JobQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(quartzJobService.queryAllLog(criteria, pageable));
    }

    /**
     * <p>create.</p>
     *
     * @param resources a {@link QuartzJobDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "新增", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "定时"
    )
    @ApiOperation("新增定时任务")
    @PostMapping
    @PreAuthorize("@el.check('timing:add')")
    public R create(@Validated @RequestBody QuartzJobDO resources) {
        Assert.isNull(resources.getId(), "A new " + ENTITY_NAME + " cannot already have an ID");
        quartzJobService.create(resources);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param resources a {@link QuartzJobDO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "修改", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "定时"
    )
    @ApiOperation("修改定时任务")
    @PutMapping
    @PreAuthorize("@el.check('timing:edit')")
    public R update(@Validated @RequestBody QuartzJobDO resources) {
        quartzJobService.update(resources);
        return R.ok();
    }

    /**
     * <p>update.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "更改定时任务状态", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "定时"
    )
    @ApiOperation("更改定时任务状态")
    @PutMapping(value = "/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public R update(@PathVariable Long id) {
        quartzJobService.updateIsPause(quartzJobService.findById(id));
        return R.ok();
    }

    /**
     * <p>getById.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @GetMapping("/{id}")
    @PreAuthorize("@el.check('timing:list')")
    public R<QuartzJobDO> getById(@PathVariable Long id) {
        return R.ok(quartzJobService.findById(id));
    }

    /**
     * <p>execution.</p>
     *
     * @param id a {@link java.lang.Long} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "执行", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "定时"
    )
    @ApiOperation("执行定时任务")
    @PutMapping(value = "/exec/{id}")
    @PreAuthorize("@el.check('timing:edit')")
    public R execution(@PathVariable Long id) {
        quartzJobService.execution(quartzJobService.findById(id));
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
            type = LogType.DELETE, module = "定时"
    )
    @ApiOperation("删除定时任务")
    @DeleteMapping
    @PreAuthorize("@el.check('timing:del')")
    public R delete(@RequestBody Set<Long> ids) {
        quartzJobService.delete(ids);
        return R.ok();
    }
}
