package org.jjche.bpm.modules.definition.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.definition.api.dto.BpmProcessInstanceCreateReqDTO;
import org.jjche.bpm.modules.task.service.BpmProcessInstanceService;
import org.jjche.core.base.BaseController;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Api(tags = "工作流 - 流程实例") // 流程实例，通过流程定义创建的一次“申请”
@RestController
@RequestMapping("/internal/process-instance")
@Validated
@RequiredArgsConstructor
public class BpmProcessInstanceApiController extends BaseController {

    private final BpmProcessInstanceService processInstanceService;

    @ApiOperation("创建")
    @PostMapping("/create")
    public String createProcessInstance(@RequestParam Long userId, @Valid @RequestBody BpmProcessInstanceCreateReqDTO reqDTO) {
        return processInstanceService.createProcessInstance(userId, reqDTO);
    }
}
