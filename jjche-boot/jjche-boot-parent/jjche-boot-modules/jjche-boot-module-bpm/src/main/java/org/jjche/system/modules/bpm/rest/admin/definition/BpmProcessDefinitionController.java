package org.jjche.system.modules.bpm.rest.admin.definition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.process.BpmProcessDefinitionListReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.process.BpmProcessDefinitionPageItemRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.process.BpmProcessDefinitionPageReqVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.process.BpmProcessDefinitionRespVO;
import org.jjche.system.modules.bpm.service.definition.BpmProcessDefinitionService;
import org.jjche.common.param.MyPage;
import org.jjche.common.wrapper.response.R;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@Tag(name = "管理后台 - 流程定义")
@SysRestController
@RequestMapping("/bpm/process-definition")
@Validated
public class BpmProcessDefinitionController {

    @Resource
    private BpmProcessDefinitionService bpmDefinitionService;

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
        String bpmnXML = bpmDefinitionService.getProcessDefinitionBpmnXML(id);
        return R.ok(bpmnXML);
    }
}
