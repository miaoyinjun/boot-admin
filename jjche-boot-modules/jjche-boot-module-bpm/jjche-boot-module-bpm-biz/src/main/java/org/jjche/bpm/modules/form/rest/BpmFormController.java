package org.jjche.bpm.modules.form.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.bpm.modules.form.dto.BpmFormDTO;
import org.jjche.bpm.modules.form.dto.BpmFormQueryCriteriaDTO;
import org.jjche.bpm.modules.form.vo.BpmFormDetailRespVO;
import org.jjche.bpm.modules.form.vo.BpmFormRespVO;
import org.jjche.bpm.modules.form.vo.BpmFormSimpleRespVO;
import org.jjche.bpm.modules.form.service.BpmFormService;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Api(tags = "工作流 - 动态表单")
@RestController
@RequestMapping("/form")
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
        return R.ok(formService.getForm(id));
    }

    @GetMapping("/list-all-simple")
    @ApiOperation(value = "获得动态表单的精简列表", notes = "用于表单下拉框")
    public R<List<BpmFormSimpleRespVO>> getSimpleForms() {
        return R.ok(formService.getFormList());
    }

    @GetMapping
    @ApiOperation(value = "获得动态表单分页")
    @PreAuthorize("@el.check('bpm:form:query')")
    public R<MyPage<BpmFormRespVO>> getFormPage(PageParam page, BpmFormQueryCriteriaDTO pageVO) {
        return R.ok(formService.getFormPage(page, pageVO));
    }
}
