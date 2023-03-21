package org.jjche.system.modules.bpm.rest.admin.definition;

import cn.hutool.core.io.IoUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.jjche.common.param.MyPage;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.system.modules.bpm.convert.definition.BpmModelConvert;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.model.*;
import org.jjche.system.modules.bpm.service.definition.BpmModelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.IOException;


@Tag(name = "管理后台 - 流程模型")
@SysRestController
@RequestMapping("/bpm/model")
@Validated
public class BpmModelController {

    @Resource
    private BpmModelService modelService;

    @GetMapping("/page")
    @Operation(summary = "获得模型分页")
    public R<MyPage<BpmModelPageItemRespVO>> getModelPage(BpmModelPageReqVO pageVO) {
        return R.ok(modelService.getModelPage(pageVO));
    }

    @GetMapping("/get")
    @Operation(summary = "获得模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@el.check('bpm:model:query')")
    public R<BpmModelRespVO> getModel(@RequestParam("id") String id) {
        BpmModelRespVO model = modelService.getModel(id);
        return R.ok(model);
    }

    @PostMapping("/create")
    @Operation(summary = "新建模型")
    @PreAuthorize("@el.check('bpm:model:create')")
    public R<String> createModel(@Valid @RequestBody BpmModelCreateReqVO createRetVO) {
        return R.ok(modelService.createModel(createRetVO, null));
    }

    @PutMapping("/update")
    @Operation(summary = "修改模型")
    @PreAuthorize("@el.check('bpm:model:update')")
    public R<Boolean> updateModel(@Valid @RequestBody BpmModelUpdateReqVO modelVO) {
        modelService.updateModel(modelVO);
        return R.ok(true);
    }

    @PostMapping("/import")
    @Operation(summary = "导入模型")
    @PreAuthorize("@el.check('bpm:model:import')")
    public R<String> importModel(@Valid BpmModeImportReqVO importReqVO) throws IOException {
        BpmModelCreateReqVO createReqVO = BpmModelConvert.INSTANCE.convert(importReqVO);
        // 读取文件
        String bpmnXml = IoUtil.readUtf8(importReqVO.getBpmnFile().getInputStream());
        return R.ok(modelService.createModel(createReqVO, bpmnXml));
    }

    @PostMapping("/deploy")
    @Operation(summary = "部署模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@el.check('bpm:model:deploy')")
    public R<Boolean> deployModel(@RequestParam("id") String id) {
        modelService.deployModel(id);
        return R.ok(true);
    }

    @PutMapping("/update-state")
    @Operation(summary = "修改模型的状态", description = "实际更新的部署的流程定义的状态")
    @PreAuthorize("@el.check('bpm:model:update')")
    public R<Boolean> updateModelState(@Valid @RequestBody BpmModelUpdateStateReqVO reqVO) {
        modelService.updateModelState(reqVO.getId(), reqVO.getState());
        return R.ok(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除模型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@el.check('bpm:model:delete')")
    public R<Boolean> deleteModel(@RequestParam("id") String id) {
        modelService.deleteModel(id);
        return R.ok(true);
    }
}
