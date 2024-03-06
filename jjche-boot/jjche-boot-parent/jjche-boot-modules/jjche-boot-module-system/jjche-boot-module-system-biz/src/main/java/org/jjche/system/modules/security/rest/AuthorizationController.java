package org.jjche.system.modules.security.rest;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jjche.cache.service.RedisService;
import org.jjche.common.dto.*;
import org.jjche.common.enums.LogCategoryType;
import org.jjche.common.enums.LogModule;
import org.jjche.common.enums.LogType;
import org.jjche.common.enums.UserTypeEnum;
import org.jjche.common.util.RsaUtils;
import org.jjche.common.wrapper.response.R;
import org.jjche.core.annotation.controller.SysRestController;
import org.jjche.core.base.BaseController;
import org.jjche.log.biz.starter.annotation.LogRecord;
import org.jjche.security.annotation.rest.IgnoreDeleteMapping;
import org.jjche.security.annotation.rest.IgnoreGetMapping;
import org.jjche.security.annotation.rest.IgnorePostMapping;
import org.jjche.security.property.*;
import org.jjche.security.security.TokenProvider;
import org.jjche.system.modules.security.api.dto.AuthUserDTO;
import org.jjche.system.modules.security.api.dto.AuthUserSmsDTO;
import org.jjche.system.modules.security.api.dto.SmsCodeDTO;
import org.jjche.system.modules.security.api.vo.LoginCodeVO;
import org.jjche.system.modules.system.service.AuthService;
import org.jjche.system.modules.system.service.UserService;
import org.jjche.system.modules.security.api.vo.LoginVO;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * <p>AuthorizationController class.</p>
 *
 * @author Zheng Jie
 * @version 1.0.8-SNAPSHOT
 * @since 2018-11-23
 * 授权、根据token获取用户详细信息
 */
@SysRestController("auth")
@RequiredArgsConstructor
@Api(tags = "系统：系统授权")
public class AuthorizationController extends BaseController {
    private final SecurityProperties properties;
    private final RedisService redisService;
    private final AuthService authService;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final CaptchaService captchaService;

    /**
     * <p>login.</p>
     *
     * @param authUser a {@link AuthUserDTO} object.
     * @return a {@link R} object.
     */
    @IgnorePostMapping(value = "/login")
    @ApiOperation("密码登录授权")
    @LogRecord(value = "密码登录", category = LogCategoryType.MANAGER, type = LogType.SELECT, module = LogModule.LOG_MODULE_LOGIN, operatorId = "{{#authUser.username}}", saveParams = false)
    public R<LoginVO> login(@Validated @RequestBody AuthUserDTO authUser) {
        SecurityRsaProperties rsaProperties = properties.getRsa();
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(rsaProperties.getPrivateKey(), authUser.getPassword());

        String paramCode = authUser.getCode();
        String captchaVerification = authUser.getCaptchaVerification();
        Assert.isTrue(StrUtil.isNotBlank(paramCode) || StrUtil.isNotBlank(captchaVerification), "请选择验证码-手输或滑动");
        //验证码-手输
        if (StrUtil.isNotBlank(paramCode)) {
            // 查询验证码-手输
            String code = redisService.stringGetString(authUser.getUuid());
            // 清除验证码
            redisService.delete(authUser.getUuid());
            Assert.notBlank(code, "验证码不存在或已过期");
            Assert.isTrue(StrUtil.equalsIgnoreCase(paramCode, code), "验证码错误");
        }//滑动
        else {
            CaptchaVO captchaVO = new CaptchaVO();
            captchaVO.setCaptchaVerification(captchaVerification);
            ResponseModel response = captchaService.verification(captchaVO);
            Assert.isTrue(response.isSuccess(), response.getRepMsg());
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
        LoginVO loginVO = userService.loginByAuthenticationToken(authenticationToken, UserTypeEnum.PWD);
        return R.ok(loginVO);
    }

    /**
     * <p>smslogin.</p>
     *
     * @param dto a {@link AuthUserSmsDTO} object.
     * @return a {@link R} object.
     */
    @IgnorePostMapping(value = "/sms_login")
    @ApiOperation("短信登录授权")
    @LogRecord(value = "短信登录", category = LogCategoryType.MANAGER, type = LogType.SELECT, module = LogModule.LOG_MODULE_LOGIN, operatorId = "{{#dto.phone}}", saveParams = false)
    public R<LoginVO> smsLogin(@Validated @RequestBody AuthUserSmsDTO dto) {
        return R.ok(userService.smslogin(dto));
    }

    /**
     * <p>getSmsCode.</p>
     *
     * @param dto a {@link SmsCodeDTO} object.
     * @return a {@link R} object.
     */
    @ApiOperation(value = "认证-获取手机验证码")
    @IgnoreGetMapping(value = "/sms_code")
    public R<String> getSmsCode(@Valid SmsCodeDTO dto) {
        return R.ok(userService.getSmsCode(dto));
    }

    /**
     * <p>getUserInfo.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public R<UserDetails> getUserInfo() {
        return R.ok(authService.getUserDetails());
    }

    /**
     * <p>getCode.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("获取验证码")
    @IgnoreGetMapping(value = "/code")
    public R<LoginCodeVO> getCode() {
        // 获取运算的结果
        SecurityLoginProperties loginProperties = properties.getLogin();
        Captcha captcha = loginProperties.getCaptcha();
        SecurityJwtProperties securityJwtProperties = properties.getJwt();
        String uuid = securityJwtProperties.getCodeKey() + IdUtil.simpleUUID();
        //当验证码类型为 arithmetic时且长度 >= 2 时，captcha.text()的结果有几率为浮点型
        String captchaValue = captcha.text();
        String dotStr = ".";
        if (captcha.getCharType() - 1 == LoginCodeEnum.arithmetic.ordinal() && captchaValue.contains(dotStr)) {
            captchaValue = captchaValue.split("\\.")[0];
        }
        // 保存
        redisService.stringSetString(uuid, captchaValue, loginProperties.getLoginCode().getExpiration());
        // 验证码信息
        LoginCodeVO loginCodeVO = new LoginCodeVO(captcha.toBase64(), uuid);
        return R.ok(loginCodeVO);
    }

    /**
     * <p>logout.</p>
     *
     * @return a {@link R} object.
     */
    @ApiOperation("退出登录")
    @IgnoreDeleteMapping("/logout")
    public R logout() {
        authService.logoutOnlineUser(tokenProvider.resolveToken());
        return R.ok();
    }

    @GetMapping("logout-token")
    public void logoutOnlineUser(String token) {
        this.authService.logoutOnlineUser(token);
    }

    @IgnoreGetMapping("user-details")
    public JwtUserDTO getUserDetails() {
        return this.authService.getUserDetails();
    }
}
