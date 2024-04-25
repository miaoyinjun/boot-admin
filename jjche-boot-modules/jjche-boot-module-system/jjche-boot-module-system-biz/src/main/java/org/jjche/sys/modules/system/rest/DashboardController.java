package org.jjche.sys.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.sys.modules.system.vo.DashboardChartVO;
import org.jjche.sys.modules.system.vo.DashboardCountVO;
import org.jjche.sys.modules.system.service.DashboardService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>MonitorController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2020-05-02
 */
@Api(tags = "系统：首页")
@RestController
@RequestMapping("dashboard")
@RequiredArgsConstructor
public class DashboardController extends BaseController {

    private final DashboardService dashboardService;

    /**
     * <p>pvIncr.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("首页-记录pv")
    @PostMapping("/pvIncr")
    public R pvIncr() {
        dashboardService.pvIncr();
        return R.ok();
    }

    /**
     * <p>query.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("首页-统计")
    @GetMapping("/count")
    @PreAuthorize("@el.check('dashboard:list')")
    public R<DashboardCountVO> count() {
        return R.ok(dashboardService.count());
    }


    /**
     * <p>chart.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("首页-图表")
    @GetMapping("/chart")
    @PreAuthorize("@el.check('dashboard:list')")
    public R<DashboardChartVO> chart() {
        return R.ok(dashboardService.chart());
    }
}
