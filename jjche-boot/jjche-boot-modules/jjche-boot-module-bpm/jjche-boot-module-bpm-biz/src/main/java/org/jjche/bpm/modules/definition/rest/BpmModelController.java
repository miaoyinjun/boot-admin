package org.jjche.bpm.modules.definition.rest;

import cn.hutool.core.io.IoUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.definition.mapstruct.BpmModelConvert;
import org.jjche.bpm.modules.definition.service.BpmModelService;
import org.jjche.bpm.modules.definition.vo.model.*;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;


@Api(tags = "管理后台 - 流程模型")
@RestController
@RequestMapping("/model")
@RequiredArgsConstructor
public class BpmModelController extends BaseController {

    private final BpmModelService modelService;
    private final BpmModelConvert bpmModelConvert;

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
        return R.ok(modelService.getModel(id));
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
        BpmModelCreateReqVO createReqVO = bpmModelConvert.convert(importReqVO);
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
