package org.jjche.bpm.modules.task.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.task.service.BpmProcessInstanceService;
import org.jjche.bpm.modules.task.vo.instance.*;
import org.jjche.common.param.MyPage;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
@Api(tags = "工作流 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/process-instance")
@Validated
@RequiredArgsConstructor
public class BpmProcessInstanceController {

    private final BpmProcessInstanceService processInstanceService;

    @GetMapping("/my-page")
    @Operation(summary = "获得我的实例分页列表", description = "在【我的流程】菜单中，进行调用")
    @PreAuthorize("@el.check('bpm:process-instance:query')")
    public R<MyPage<BpmProcessInstancePageItemRespVO>> getMyProcessInstancePage(
            @Valid BpmProcessInstanceMyPageReqVO pageReqVO) {
        return R.ok(processInstanceService.getMyProcessInstancePage(SecurityUtil.getUserId(), pageReqVO));
    }

    @PostMapping("/create")
    @Operation(summary = "新建流程实例")
    @PreAuthorize("@el.check('bpm:process-instance:query')")
    public R<String> createProcessInstance(@Valid @RequestBody BpmProcessInstanceCreateReqVO createReqVO) {
        return R.ok(processInstanceService.createProcessInstance(SecurityUtil.getUserId(), createReqVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得指定流程实例", description = "在【流程详细】界面中，进行调用")
    @Parameter(name = "id", description = "流程实例的编号", required = true)
    @PreAuthorize("@el.check('bpm:process-instance:query')")
    public R<BpmProcessInstanceRespVO> getProcessInstance(@RequestParam("id") String id) {
        return R.ok(processInstanceService.getProcessInstanceVO(id));
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "取消流程实例", description = "撤回发起的流程")
    @PreAuthorize("@el.check('bpm:process-instance:cancel')")
    public R<Boolean> cancelProcessInstance(@Valid @RequestBody BpmProcessInstanceCancelReqVO cancelReqVO) {
        processInstanceService.cancelProcessInstance(SecurityUtil.getUserId(), cancelReqVO);
        return R.ok(true);
    }
}
