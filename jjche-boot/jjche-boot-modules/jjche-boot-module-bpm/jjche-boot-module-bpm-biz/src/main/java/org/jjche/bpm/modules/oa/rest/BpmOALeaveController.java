package org.jjche.bpm.modules.oa.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.oa.api.vo.BpmOALeaveCreateReqDTO;
import org.jjche.bpm.modules.oa.api.vo.BpmOALeavePageReqVO;
import org.jjche.bpm.modules.oa.api.vo.BpmOALeaveRespVO;
import org.jjche.bpm.modules.oa.domain.BpmOALeaveDO;
import org.jjche.bpm.modules.oa.mapstruct.BpmOALeaveConvert;
import org.jjche.bpm.modules.oa.service.BpmOALeaveService;
import org.jjche.common.param.MyPage;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * OA 请假申请 Controller，用于演示自己存储数据，接入工作流的例子
 *
 * @author jason
 * @author 芋道源码
 */
@Api(tags = "管理后台 - OA 请假申请")
@RestController
@RequestMapping("/oa/leave")
@Validated
@RequiredArgsConstructor
public class BpmOALeaveController {

    private final BpmOALeaveService leaveService;
    private final BpmOALeaveConvert leaveConvert;

    @PostMapping("/create")
    @PreAuthorize("@el.check('bpm:oa-leave:create')")
    @Operation(summary = "创建请求申请")
    public R<Long> createLeave(@Valid @RequestBody BpmOALeaveCreateReqDTO createReqVO) {
        return R.ok(leaveService.createLeave(SecurityUtil.getUserId(), createReqVO));
    }

    @GetMapping("/get")
    @PreAuthorize("@el.check('bpm:oa-leave:query')")
    @Operation(summary = "获得请假申请")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public R<BpmOALeaveRespVO> getLeave(@RequestParam("id") Long id) {
        BpmOALeaveDO leave = leaveService.getLeave(id);
        return R.ok(leaveConvert.toVO(leave));
    }

    @GetMapping("/page")
    @PreAuthorize("@el.check('bpm:oa-leave:query')")
    @Operation(summary = "获得请假申请分页")
    public R<MyPage<BpmOALeaveRespVO>> getLeavePage(@Valid BpmOALeavePageReqVO pageVO) {
        MyPage<BpmOALeaveDO> pageResult = leaveService.getLeavePage(SecurityUtil.getUserId(), pageVO);
        return R.ok(leaveConvert.toVO(pageResult));
    }

}
