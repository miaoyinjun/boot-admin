package org.jjche.bpm.modules.definition.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleCreateReqVO;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleRespVO;
import org.jjche.bpm.modules.definition.vo.rule.BpmTaskAssignRuleUpdateReqVO;
import org.jjche.bpm.modules.definition.service.BpmTaskAssignRuleService;
import org.jjche.common.wrapper.response.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "工作流 - 任务分配规则")
@RestController
@RequestMapping("/task-assign-rule")
@Validated
@RequiredArgsConstructor
public class BpmTaskAssignRuleController {

    private final BpmTaskAssignRuleService taskAssignRuleService;

    @GetMapping("/list")
    @Operation(summary = "获得任务分配规则列表")
    @Parameters({
            @Parameter(name = "modelId", description = "模型编号", example = "1024"),
            @Parameter(name = "processDefinitionId", description = "流程定义的编号", example = "2048")
    })
    @PreAuthorize("@el.check('bpm:task-assign-rule:query')")
    public R<List<BpmTaskAssignRuleRespVO>> getTaskAssignRuleList(
            @RequestParam(value = "modelId", required = false) String modelId,
            @RequestParam(value = "processDefinitionId", required = false) String processDefinitionId) {
        return R.ok(taskAssignRuleService.getTaskAssignRuleList(modelId, processDefinitionId));
    }

    @PostMapping("/create")
    @Operation(summary = "创建任务分配规则")
    @PreAuthorize("@el.check('bpm:task-assign-rule:create')")
    public R<Long> createTaskAssignRule(@Valid @RequestBody BpmTaskAssignRuleCreateReqVO reqVO) {
        return R.ok(taskAssignRuleService.createTaskAssignRule(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新任务分配规则")
    @PreAuthorize("@el.check('bpm:task-assign-rule:update')")
    public R<Boolean> updateTaskAssignRule(@Valid @RequestBody BpmTaskAssignRuleUpdateReqVO reqVO) {
        taskAssignRuleService.updateTaskAssignRule(reqVO);
        return R.ok(true);
    }
}
