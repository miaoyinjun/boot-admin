package org.jjche.demo.modules.student.rest;

import com.github.xiaoymin.knife4j.annotations.ApiOperationSupport;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.jjche.common.base.BaseDTO;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.core.excel.ExcelImportTemplate;
import org.jjche.core.vo.ExcelImportRetVO;
import org.jjche.demo.constant.ApiVersion;
import org.jjche.demo.modules.student.api.dto.StudentDTO;
import org.jjche.demo.modules.student.api.dto.StudentQueryCriteriaDTO;
import org.jjche.demo.modules.student.api.enums.CourseEnum;
import org.jjche.demo.modules.student.api.vo.StudentVO;
import org.jjche.demo.modules.student.service.StudentImportService;
import org.jjche.demo.modules.student.service.StudentService;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;

/**
 * <p>
 * 学生 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "学生")
@ApiSupport(order = 1, author = "miaoyj")
@RestController
@RequestMapping("students")
@RequiredArgsConstructor
public class StudentController extends BaseController {

    private final StudentService studentService;
    private final Validator globalValidator;

    @PostMapping
    @ApiOperation(value = "学生-新增", tags = ApiVersion.VERSION_1_0_0)
    @ApiOperationSupport(ignoreParameters = {"id"})
    @PreAuthorize("@el.check('student:add')")
    @LogRecord(value = "创建了一个学生, 学生姓名：「{{#dto.name}}」", category = LogCategoryType.OPERATING, type = LogType.ADD, module = ApiVersion.MODULE_STUDENT, bizNo = "{{#_ret}}")
    public R<Long> create(@Validated @Valid @RequestBody StudentDTO dto) {
        return R.ok(studentService.save(dto));
    }

    @DeleteMapping
    @ApiOperation(value = "学生-删除", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:del')")
    @LogRecord(value = "删除", category = LogCategoryType.OPERATING, type = LogType.DELETE, module = ApiVersion.MODULE_STUDENT, bizNo = "{{#ids}}", detail = "{API_MODEL{#_oldObj}} {STUDENT_DIFF_OLD_BY_ID{#ids}}")
    public R delete(@RequestBody List<Long> ids) {
        studentService.delete(ids);
        return R.ok();
    }

    @PutMapping
    @ApiOperation(value = "学生-修改", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:edit')")
    @LogRecord(value = "被修改的学生姓名：「{{#dto.name}}」", category = LogCategoryType.OPERATING, type = LogType.UPDATE, module = ApiVersion.MODULE_STUDENT, bizNo = "{{#dto.id}}", detail = "{_DIFF{#dto}} {STUDENT_DIFF_OLD_BY_ID{#dto.id}}")
    public R update(@Validated(BaseDTO.Update.class) @Valid @RequestBody StudentDTO dto) {
        studentService.update(dto);
        return R.ok();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "学生-查询单个", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    public R<StudentVO> getById(@PathVariable Long id) {
        return R.ok(this.studentService.getVoById(id));
    }

    @GetMapping
    @ApiOperation(value = "学生-列表", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    public R<MyPage<StudentVO>> page(PageParam page, @ApiParam(value = "课程") @RequestParam(required = false) CourseEnum course, @Validated StudentQueryCriteriaDTO query) {
        return R.ok(studentService.page(page, course, query));
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation(value = "学生-导出", tags = ApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    public void download(StudentQueryCriteriaDTO criteria) {
        studentService.download(criteria);
    }

    @ApiOperation(value = "学生-导入")
    @PostMapping(value = "/import")
    @PreAuthorize("@el.check('student:add')")
    public R<List<ExcelImportRetVO>> importStudent(@RequestPart("file") MultipartFile file) {
        ExcelImportTemplate importTemplate = new StudentImportService(file, studentService, globalValidator);
        return importTemplate.importData();
    }
}
