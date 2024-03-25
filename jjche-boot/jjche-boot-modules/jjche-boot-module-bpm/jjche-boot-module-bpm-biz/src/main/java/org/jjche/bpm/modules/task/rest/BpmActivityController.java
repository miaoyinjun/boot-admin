package org.jjche.bpm.modules.task.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.task.vo.activity.BpmActivityRespVO;
import org.jjche.bpm.modules.task.service.BpmActivityService;
import org.jjche.common.wrapper.response.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Api(tags = "管理后台 - 流程活动实例")
@RestController
@RequestMapping("/activity")
@Validated
@RequiredArgsConstructor
public class BpmActivityController {

    private final BpmActivityService activityService;

    @GetMapping("/list")
    @Operation(summary = "生成指定流程实例的高亮流程图",
            description = "只高亮进行中的任务。不过要注意，该接口暂时没用，通过前端的 ProcessViewer.vue 界面的 highlightDiagram 方法生成")
    @Parameter(name = "processInstanceId", description = "流程实例的编号", required = true)
    @PreAuthorize("@el.check('bpm:task:query')")
    public R<List<BpmActivityRespVO>> getActivityList(
            @RequestParam("processInstanceId") String processInstanceId) {
        return R.ok(activityService.getActivityListByProcessInstanceId(processInstanceId));
    }
}
