package org.jjche.system.modules.bpm.rest.admin.oa;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.system.modules.bpm.rest.admin.oa.vo.BpmOALeaveCreateReqVO;
import org.jjche.system.modules.bpm.rest.admin.oa.vo.BpmOALeavePageReqVO;
import org.jjche.system.modules.bpm.rest.admin.oa.vo.BpmOALeaveRespVO;
import org.jjche.system.modules.bpm.convert.oa.BpmOALeaveConvert;
import org.jjche.system.modules.bpm.dal.dataobject.oa.BpmOALeaveDO;
import org.jjche.system.modules.bpm.service.oa.BpmOALeaveService;
import org.jjche.common.param.MyPage;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * OA 请假申请 Controller，用于演示自己存储数据，接入工作流的例子
 *
 * @author jason
 * @author 芋道源码
 */
@Tag(name = "管理后台 - OA 请假申请")
@SysRestController
@RequestMapping("/bpm/oa/leave")
@Validated
public class BpmOALeaveController {

    @Resource
    private BpmOALeaveService leaveService;

    @PostMapping("/create")
    @PreAuthorize("@el.check('bpm:oa-leave:create')")
    @Operation(summary = "创建请求申请")
    public R<Long> createLeave(@Valid @RequestBody BpmOALeaveCreateReqVO createReqVO) {
        return R.ok(leaveService.createLeave(SecurityUtil.getUserId(), createReqVO));
    }

    @GetMapping("/get")
    @PreAuthorize("@el.check('bpm:oa-leave:query')")
    @Operation(summary = "获得请假申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public R<BpmOALeaveRespVO> getLeave(@RequestParam("id") Long id) {
        BpmOALeaveDO leave = leaveService.getLeave(id);
        return R.ok(BpmOALeaveConvert.INSTANCE.convert(leave));
    }

    @GetMapping("/page")
    @PreAuthorize("@el.check('bpm:oa-leave:query')")
    @Operation(summary = "获得请假申请分页")
    public R<MyPage<BpmOALeaveRespVO>> getLeavePage(@Valid BpmOALeavePageReqVO pageVO) {
        MyPage<BpmOALeaveDO> pageResult = leaveService.getLeavePage(SecurityUtil.getUserId(), pageVO);
        return R.ok(BpmOALeaveConvert.INSTANCE.convertPage(pageResult));
    }

}
