package org.jjche.system.modules.bpm.rest.admin.task;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jjche.common.param.MyPage;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.util.SecurityUtil;
import org.jjche.system.modules.bpm.rest.admin.task.vo.task.*;
import org.jjche.system.modules.bpm.service.task.BpmTaskService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Tag(name = "管理后台 - 流程任务实例")
@SysRestController
@RequestMapping("/bpm/task")
@Validated
public class BpmTaskController {

    @Resource
    private BpmTaskService taskService;

    @GetMapping("todo-page")
    @Operation(summary = "获取 Todo 待办任务分页")
    @PreAuthorize("@el.check('bpm:task:query')")
    public R<MyPage<BpmTaskTodoPageItemRespVO>> getTodoTaskPage(@Valid BpmTaskTodoPageReqVO pageVO) {
        return R.ok(taskService.getTodoTaskPage(SecurityUtil.getUserId(), pageVO));
    }

    @GetMapping("done-page")
    @Operation(summary = "获取 Done 已办任务分页")
    @PreAuthorize("@el.check('bpm:task:query')")
    public R<MyPage<BpmTaskDonePageItemRespVO>> getDoneTaskPage(@Valid BpmTaskDonePageReqVO pageVO) {
        return R.ok(taskService.getDoneTaskPage(SecurityUtil.getUserId(), pageVO));
    }

    @GetMapping("/list-by-process-instance-id")
    @Operation(summary = "获得指定流程实例的任务列表", description = "包括完成的、未完成的")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
    @PreAuthorize("@el.check('bpm:task:query')")
    public R<List<BpmTaskRespVO>> getTaskListByProcessInstanceId(
        @RequestParam("processInstanceId") String processInstanceId) {
        return R.ok(taskService.getTaskListByProcessInstanceId(processInstanceId));
    }

    @PutMapping("/approve")
    @Operation(summary = "通过任务")
    @PreAuthorize("@el.check('bpm:task:update')")
    public R<Boolean> approveTask(@Valid @RequestBody BpmTaskApproveReqVO reqVO) {
        taskService.approveTask(SecurityUtil.getUserId(), reqVO);
        return R.ok(true);
    }

    @PutMapping("/reject")
    @Operation(summary = "不通过任务")
    @PreAuthorize("@el.check('bpm:task:update')")
    public R<Boolean> rejectTask(@Valid @RequestBody BpmTaskRejectReqVO reqVO) {
        taskService.rejectTask(SecurityUtil.getUserId(), reqVO);
        return R.ok(true);
    }

    @PutMapping("/update-assignee")
    @Operation(summary = "更新任务的负责人", description = "用于【流程详情】的【转派】按钮")
    @PreAuthorize("@el.check('bpm:task:update')")
    public R<Boolean> updateTaskAssignee(@Valid @RequestBody BpmTaskUpdateAssigneeReqVO reqVO) {
        taskService.updateTaskAssignee(SecurityUtil.getUserId(), reqVO);
        return R.ok(true);
    }

}
