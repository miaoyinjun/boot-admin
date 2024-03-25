package org.jjche.demo.modules.student.api;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.demo.constant.ApiVersion;
import org.jjche.demo.modules.student.dto.StudentQueryCriteriaDTO;
import org.jjche.demo.modules.student.api.enums.StudentCourseEnum;
import org.jjche.demo.modules.student.api.vo.StudentApiVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("internal/students")
@RequiredArgsConstructor
public class StudentApiController extends BaseController {

    private final IDemoStudentApi studentApiService;
    @GetMapping
    @ApiOperation(value = "对内-学生-列表", tags = ApiVersion.VERSION_1_0_0)
    public R<MyPage<StudentApiVO>> page(PageParam page, @ApiParam(value = "课程") @RequestParam(required = false) StudentCourseEnum course, @Validated StudentQueryCriteriaDTO query) {
        return studentApiService.page(page, course, query.getName());
    }

}
