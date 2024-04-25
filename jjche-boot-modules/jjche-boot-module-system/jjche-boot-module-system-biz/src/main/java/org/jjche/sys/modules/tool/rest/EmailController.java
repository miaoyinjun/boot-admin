package org.jjche.sys.modules.tool.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogType;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.sys.modules.tool.vo.EmailVO;
import org.jjche.sys.modules.tool.domain.EmailConfigDO;
import org.jjche.sys.modules.tool.service.EmailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 发送邮件
 *
 * @author 郑杰
 * @version 1.0.8-SNAPSHOT
 * @since 2018/09/28 6:55:53
 */
@Api(tags = "工具：邮件管理")
@RestController
@RequestMapping("email")
@RequiredArgsConstructor
public class EmailController extends BaseController {

    private final EmailService emailService;

    /**
     * <p>queryConfig.</p>
     *
     * @return a {@link R} object.
     */
    @GetMapping
    public R<EmailConfigDO> queryConfig() {
        return R.ok(emailService.find());
    }

    /**
     * <p>updateConfig.</p>
     *
     * @param emailConfig a {@link EmailConfigDO} object.
     * @return a {@link R} object.
     * @throws Exception if any.
     */
    @LogRecord(
            value = "配置", category = LogCategoryType.MANAGER,
            type = LogType.UPDATE, module = "邮件"
    )
    @PutMapping
    @ApiOperation("配置邮件")
    public R updateConfig(@Validated @RequestBody EmailConfigDO emailConfig) throws Exception {
        emailService.config(emailConfig, emailService.find());
        return R.ok();
    }

    /**
     * <p>sendEmail.</p>
     *
     * @param emailVo a {@link EmailVO} object.
     * @return a {@link R} object.
     */
    @LogRecord(
            value = "发送", category = LogCategoryType.MANAGER,
            type = LogType.ADD, module = "邮件"
    )
    @PostMapping
    @ApiOperation("发送邮件")
    public R sendEmail(@Validated @RequestBody EmailVO emailVo) {
        emailService.send(emailVo, emailService.find());
        return R.ok();
    }
}
