package org.jjche.demo.modules.provider.rest;

import cn.hutool.log.StaticLog;
import com.github.xiaoymin.knife4j.annotations.ApiSupport;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.param.MyPage;
import org.jjche.common.param.PageParam;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.ApiRestController;
import org.jjche.core.base.BaseController;
import org.jjche.demo.constant.ProviderApiVersion;
import org.jjche.demo.modules.provider.api.enums.ProviderCourseEnum;
import org.jjche.demo.modules.provider.api.vo.ProviderVO;
import org.jjche.demo.modules.provider.api.ProviderStudentApi;
import org.jjche.demo.modules.provider.service.ProviderService;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 学生 控制器
 * </p>
 *
 * @author miaoyj
 * @version 1.0.0-SNAPSHOT
 * @since 2021-02-02
 */
@Api(tags = "提供者")
@ApiSupport(order = 1, author = "miaoyj")
@ApiRestController(ProviderApiVersion.API_STUDENTS)
@RequiredArgsConstructor
public class ProviderStudentStudentController extends BaseController implements ProviderStudentApi {

    private final ProviderService providerService;

    @GetMapping
    @ApiOperation(value = "学生-列表", tags = ProviderApiVersion.VERSION_1_0_0)
    @PreAuthorize("@el.check('student:list')")
    @LogRecord(
            value = "被调用", category = LogCategoryType.OPERATING,
            type = LogType.SELECT, module = "学生"
    )
    public R<MyPage<ProviderVO>> page(PageParam page,
                                      @ApiParam(value = "课程")
                                      @RequestParam(required = false) ProviderCourseEnum course,
                                      @ApiParam(value = "姓名", example = "大")
                                      @RequestParam(required = false) String name) {
        StaticLog.warn("provider.name:{}", name);
        return R.ok(providerService.page(page, course, name));
    }

}
