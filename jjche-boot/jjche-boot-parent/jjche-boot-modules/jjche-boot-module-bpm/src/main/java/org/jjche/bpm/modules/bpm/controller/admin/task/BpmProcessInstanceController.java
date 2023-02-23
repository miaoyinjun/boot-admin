package org.jjche.bpm.modules.bpm.controller.admin.task;

import cn.hutool.db.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jjche.bpm.modules.bpm.controller.admin.task.vo.instance.*;
import org.jjche.bpm.modules.bpm.service.task.BpmProcessInstanceService;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.util.SecurityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
@Tag(name = "管理后台 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/bpm/process-instance")
@Validated
public class BpmProcessInstanceController {

    @Resource
    private BpmProcessInstanceService processInstanceService;

    @GetMapping("/my-page")
    @Operation(summary = "获得我的实例分页列表", description = "在【我的流程】菜单中，进行调用")
    @PreAuthorize("@el.check('bpm:process-instance:query')")
    public R<PageResult<BpmProcessInstancePageItemRespVO>> getMyProcessInstancePage(
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
