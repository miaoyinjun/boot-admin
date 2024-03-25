package org.jjche.demo.modules.student.rest;

import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.constant.FilterEncConstant;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.OutRestController;
import org.jjche.core.base.BaseController;
import org.jjche.demo.constant.ApiVersion;
import org.jjche.demo.modules.student.dto.StudentDTO;
import org.jjche.demo.modules.student.service.StudentService;
import org.jjche.filter.enc.api.enums.HttpBodyDecrypt;
import org.jjche.filter.enc.api.enums.HttpResDataEncrypt;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.security.annotation.rest.IgnorePostMapping;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>
 * 学生-对外 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "对外-加密|解密")
@ApiSupport(order = 1, author = "miaoyj")
@OutRestController("students")
@RequiredArgsConstructor
public class OutStudentController extends BaseController {

    private final StudentService studentService;

    @IgnorePostMapping(value = "add")
    @ApiOperation(value = "添加-出参加密，入参解密")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = FilterEncConstant.PARAM_TYPE_HEADER, name = FilterEncConstant.APP_ID, value = FilterEncConstant.APP_ID_DESC, example = FilterEncConstant.DEFAULT_APP_ID, dataTypeClass = String.class, required = true),
            @ApiImplicitParam(paramType = FilterEncConstant.PARAM_TYPE_HEADER, name = FilterEncConstant.TIMESTAMP, value = FilterEncConstant.TIMESTAMP_DESC, dataTypeClass = String.class, required = true),
            @ApiImplicitParam(paramType = FilterEncConstant.PARAM_TYPE_HEADER, name = FilterEncConstant.NONCE, value = FilterEncConstant.NONCE_DESC, example = "1234567890", dataTypeClass = String.class, required = true),
            @ApiImplicitParam(paramType = FilterEncConstant.PARAM_TYPE_HEADER, name = FilterEncConstant.SIGN, value = FilterEncConstant.SIGN_DESC, dataTypeClass = String.class, required = true),
    })
    @HttpBodyDecrypt
    @HttpResDataEncrypt
    @LogRecord(value = "创建了一个学生, 学生姓名：「{{#dto.name}}」", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = ApiVersion.MODULE_STUDENT, operatorId = "{{#appId}}")
    public R<StudentDTO> post(@Validated @Valid @RequestBody StudentDTO dto) {
        return R.ok(dto);
    }
}
