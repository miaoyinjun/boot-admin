package org.jjche.bpm.modules.definition.rest;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.definition.vo.process.BpmProcessDefinitionListReqVO;
import org.jjche.bpm.modules.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import org.jjche.bpm.modules.definition.vo.process.BpmProcessDefinitionPageReqVO;
import org.jjche.bpm.modules.definition.vo.process.BpmProcessDefinitionRespVO;
import org.jjche.bpm.modules.definition.service.BpmProcessDefinitionService;
import org.jjche.common.param.MyPage;
import org.jjche.common.wrapper.response.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = "工作流 - 流程定义")
@RestController
@RequestMapping("/process-definition")
@Validated
@RequiredArgsConstructor
public class BpmProcessDefinitionController {

    private final BpmProcessDefinitionService bpmDefinitionService;

    @GetMapping("/page")
    @Operation(summary = "获得流程定义分页")
    @PreAuthorize("@el.check('bpm:process-definition:query')")
    public R<MyPage<BpmProcessDefinitionPageItemRespVO>> getProcessDefinitionPage(
            BpmProcessDefinitionPageReqVO pageReqVO) {
        return R.ok(bpmDefinitionService.getProcessDefinitionPage(pageReqVO));
    }

    @GetMapping ("/list")
    @Operation(summary = "获得流程定义列表")
    @PreAuthorize("@el.check('bpm:process-definition:query')")
    public R<List<BpmProcessDefinitionRespVO>> getProcessDefinitionList(
            BpmProcessDefinitionListReqVO listReqVO) {
        return R.ok(bpmDefinitionService.getProcessDefinitionList(listReqVO));
    }

    @GetMapping ("/get-bpmn-xml")
    @Operation(summary = "获得流程定义的 BPMN XML")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@el.check('bpm:process-definition:query')")
    public R<String> getProcessDefinitionBpmnXML(@RequestParam("id") String id) {
        return R.ok(bpmDefinitionService.getProcessDefinitionBpmnXML(id));
    }
}
