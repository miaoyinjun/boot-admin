package org.jjche.sys.modules.logging.rest;

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
import org.jjche.sys.modules.logging.api.dto.LogQueryCriteriaDTO;
import org.jjche.sys.modules.logging.api.vo.LogVO;
import org.jjche.sys.modules.logging.domain.LogDO;
import org.jjche.sys.modules.logging.service.LogService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>LogController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-24
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("logs")
@Api(tags = "系统：日志管理")
public class LogController extends BaseController {

    private final LogService logService;

    /**
     * <p>
     * download
     * </p>
     *
     * @param response 返回
     * @param criteria 条件
     * @throws IOException if any.
     */
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('log:list')")
    public void download(HttpServletResponse response, LogQueryCriteriaDTO criteria) throws IOException {
        logService.download(logService.queryAll(criteria), response);
    }

    /**
     * <p>query.</p>
     *
     * @param criteria a {@link LogQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @GetMapping
    @PreAuthorize("@el.check('log:list')")
    public R<MyPage<LogVO>> query(LogQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(logService.queryAll(criteria, pageable));
    }

    /**
     * <p>queryUserLog.</p>
     *
     * @param criteria a {@link LogQueryCriteriaDTO} object.
     * @param pageable /
     * @return a {@link R} object.
     */
    @GetMapping(value = "/user")
    @ApiOperation("用户日志查询")
    public R<MyPage<LogVO>> queryUserLog(LogQueryCriteriaDTO criteria, PageParam pageable) {
        return R.ok(logService.queryAllByUser(criteria, pageable));
    }

    /**
     * <p>queryErrorLogs.</p>
     *
     * @param id a {@link Long} object.
     * @return a {@link R} object.
     */
    @GetMapping(value = "/error/{id}")
    @ApiOperation("日志异常详情查询")
    @PreAuthorize("@el.check('log:list')")
    public R<Object> queryErrorLogs(@PathVariable Long id) {
        return R.ok(logService.findByErrDetail(id));
    }

    /**
     * <p>delAllInfoLog.</p>
     *
     * @return a {@link R} object.
     */
    @DeleteMapping(value = "/del")
    @LogRecord(
            value = "清空3个月之前的操作日志", category = LogCategoryType.MANAGER,
            type = LogType.DELETE, module = "日志"
    )
    @ApiOperation("清空3个月之前的操作日志")
    @PreAuthorize("@el.check('log:del')")
    public R delAllInfoLog() {
        logService.delAll();
        return R.ok();
    }

    /**
     * <p>listModule.</p>
     *
     * @return a {@link R} object.
     */
    @GetMapping("/modules")
    @ApiOperation("日志获取模块")
    @PreAuthorize("@el.check('log:list')")
    @Cached(name = "logs:modules", cacheType = CacheType.REMOTE, expire = 3600000)
    public R<List<String>> listModule() {
        return R.ok(logService.listModule());
    }

    /**
     * <p>listModule.</p>
     *
     * @return a {@link R} object.
     */
    @GetMapping("/appNames")
    @ApiOperation("日志获取应用名")
    @PreAuthorize("@el.check('log:list')")
    @Cached(name = "logs:appNames", cacheType = CacheType.REMOTE, expire = 3600000)
    public R<List<String>> listAppName() {
        return R.ok(logService.listAppName());
    }

    /**
     * <p>listByBizKeyAndBizNo.</p>
     *
     * @param page   a {@link PageParam} object.
     * @param bizKey a {@link String} object.
     * @param bizNo  a {@link String} object.
     * @return a {@link R} object.
     */
    @GetMapping("/biz")
    @ApiOperation("查询业务标识与业务主键")
    public R<MyPage<LogDO>> listByBizKeyAndBizNo(PageParam page,
                                                 String bizKey, String bizNo) {
        return R.ok(logService.listByBizKeyAndBizNo(page, bizKey, bizNo));
    }
}
