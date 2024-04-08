package org.jjche.sys.modules.system.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.base.BaseController;
import org.jjche.sys.modules.system.enums.CodeBiEnum;
import org.jjche.sys.modules.system.enums.CodeEnum;
import org.jjche.sys.modules.system.service.VerifyService;
import org.jjche.sys.modules.tool.vo.EmailVO;
import org.jjche.sys.modules.tool.service.EmailService;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * <p>VerifyController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-12-26
 */
@Api(tags = "系统：验证码管理")
@RestController
@RequestMapping("code")
@RequiredArgsConstructor
public class VerifyController extends BaseController {

    private final VerifyService verificationCodeService;
    private final EmailService emailService;

    /**
     * <p>resetEmail.</p>
     *
     * @param email a {@link java.lang.String} object.
     * @return a {@link R} object.
     */
    @PostMapping(value = "/resetEmail")
    @ApiOperation("重置邮箱，发送验证码")
    public R resetEmail(@RequestParam String email) {
        EmailVO emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVo, emailService.find());
        return R.ok();
    }

    /**
     * <p>resetPass.</p>
     *
     * @param email a {@link java.lang.String} object.
     * @return a {@link R} object.
     */
    @PostMapping(value = "/email/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public R resetPass(@RequestParam String email) {
        EmailVO emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVo, emailService.find());
        return R.ok();
    }

    /**
     * <p>validated.</p>
     *
     * @param email  a {@link java.lang.String} object.
     * @param code   a {@link java.lang.String} object.
     * @param codeBi a {@link java.lang.Integer} object.
     * @return a {@link R} object.
     */
    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public R validated(@RequestParam String email, @RequestParam String code, @RequestParam Integer codeBi) {
        CodeBiEnum biEnum = CodeBiEnum.find(codeBi);
        switch (Objects.requireNonNull(biEnum)) {
            case ONE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case TWO:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
                break;
            default:
                break;
        }
        return R.ok();
    }
}
