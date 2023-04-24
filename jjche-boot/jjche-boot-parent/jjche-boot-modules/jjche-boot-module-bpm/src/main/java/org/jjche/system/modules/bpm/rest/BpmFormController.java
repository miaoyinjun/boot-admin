package org.jjche.system.modules.bpm.rest;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.system.modules.bpm.convert.definition.BpmFormConvert;
import org.jjche.system.modules.bpm.dal.dataobject.definition.BpmFormDO;
import org.jjche.system.modules.bpm.rest.admin.definition.dto.form.BpmFormDTO;
import org.jjche.system.modules.bpm.rest.admin.definition.dto.form.BpmFormQueryCriteriaDTO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.form.BpmFormDetailRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.form.BpmFormRespVO;
import org.jjche.system.modules.bpm.rest.admin.definition.vo.form.BpmFormSimpleRespVO;
import org.jjche.system.modules.bpm.service.definition.BpmFormService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Tag(name = "管理后台 - 动态表单")
@SysRestController
@RequestMapping("/bpm/form")
@RequiredArgsConstructor
public class BpmFormController extends BaseController {

    private final BpmFormService formService;

    @PostMapping
    @ApiOperation(value = "创建动态表单")
    @PreAuthorize("@el.check('bpm:form:create')")
    public R createForm(@Valid @RequestBody BpmFormDTO createReqVO) {
        formService.createForm(createReqVO);
        return R.ok();
    }

    @PutMapping
    @ApiOperation(value = "更新动态表单")
    @PreAuthorize("@el.check('bpm:form:update')")
    public R updateForm(@Valid @RequestBody BpmFormDTO updateReqVO) {
        formService.updateForm(updateReqVO);
        return R.ok();
    }

    @DeleteMapping
    @ApiOperation(value = "删除动态表单")
    @PreAuthorize("@el.check('bpm:form:delete')")
    public R<Boolean> deleteForm(@RequestBody List<Long> ids) {
        formService.deleteForm(ids);
        return R.ok(true);
    }

    @GetMapping("{id}")
    @ApiOperation(value = "获得动态表单")
    @PreAuthorize("@el.check('bpm:form:query')")
    public R<BpmFormDetailRespVO> getForm(@PathVariable("id") Long id) {
        BpmFormDO form = formService.getForm(id);
        return R.ok(BpmFormConvert.INSTANCE.convert2(form));
    }

    @GetMapping("/list-all-simple")
    @ApiOperation(value = "获得动态表单的精简列表", notes = "用于表单下拉框")
    public R<List<BpmFormSimpleRespVO>> getSimpleForms() {
        List<BpmFormDO> list = formService.getFormList();
        return R.ok(BpmFormConvert.INSTANCE.convertList2(list));
    }

    @GetMapping
    @ApiOperation(value = "获得动态表单分页")
    @PreAuthorize("@el.check('bpm:form:query')")
    public R<MyPage<BpmFormRespVO>> getFormPage(PageParam page, BpmFormQueryCriteriaDTO pageVO) {
        return R.ok(formService.getFormPage(page, pageVO));
    }
}
