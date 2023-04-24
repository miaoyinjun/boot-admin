package org.jjche.system.modules.bpm.rest;

import cn.hutool.core.io.IoUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.system.modules.bpm.convert.definition.BpmModelConvert;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.model.*;
import org.jjche.system.modules.bpm.service.definition.BpmModelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@Tag(name = "管理后台 - 流程模型")
@SysRestController
@RequestMapping("/bpm/model")
@RequiredArgsConstructor
public class BpmModelController extends BaseController {

    private final BpmModelService modelService;

    @GetMapping
    @ApiOperation(value = "获得模型分页")
    public R<MyPage<BpmModelPageItemRespVO>> getModelPage(
            PageParam page, BpmModelPageReqVO pageVO) {
        return R.ok(modelService.getModelPage(page, pageVO));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "获得模型")
    @PreAuthorize("@el.check('bpm:model:query')")
    public R<BpmModelRespVO> getModel(@PathVariable String id) {
        BpmModelRespVO model = modelService.getModel(id);
        return R.ok(model);
    }

    @PostMapping
    @ApiOperation(value = "新建模型")
    @PreAuthorize("@el.check('bpm:model:create')")
    public R<String> createModel(@Valid @RequestBody BpmModelCreateReqVO createRetVO) {
        return R.ok(modelService.createModel(createRetVO, null));
    }

    @PutMapping
    @ApiOperation(value = "修改模型")
    @PreAuthorize("@el.check('bpm:model:update')")
    public R<Boolean> updateModel(@Valid @RequestBody BpmModelUpdateReqVO modelVO) {
        modelService.updateModel(modelVO);
        return R.ok(true);
    }

    @PostMapping("/import")
    @ApiOperation(value = "导入模型")
    @PreAuthorize("@el.check('bpm:model:import')")
    public R<String> importModel(@Valid BpmModeImportReqVO importReqVO) throws IOException {
        BpmModelCreateReqVO createReqVO = BpmModelConvert.INSTANCE.convert(importReqVO);
        // 读取文件
        String bpmnXml = IoUtil.readUtf8(importReqVO.getBpmnFile().getInputStream());
        return R.ok(modelService.createModel(createReqVO, bpmnXml));
    }

    @PostMapping("/deploy")
    @ApiOperation(value = "部署模型")
    @PreAuthorize("@el.check('bpm:model:deploy')")
    public R<Boolean> deployModel(@RequestParam("id") String id) {
        modelService.deployModel(id);
        return R.ok(true);
    }

    @PutMapping("state")
    @ApiOperation(value = "修改模型的状态", notes = "实际更新的部署的流程定义的状态")
    @PreAuthorize("@el.check('bpm:model:update')")
    public R<Boolean> updateModelState(@Valid @RequestBody BpmModelUpdateStateReqVO reqVO) {
        modelService.updateModelState(reqVO.getId(), reqVO.getState());
        return R.ok(true);
    }

    @DeleteMapping
    @ApiOperation(value = "删除模型")
    @PreAuthorize("@el.check('bpm:model:delete')")
    public R<Boolean> deleteModel(@RequestBody List<String> ids) {
        modelService.deleteModel(ids);
        return R.ok(true);
    }
}
